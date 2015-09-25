/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.agiletec.aps.util.ApsWebApplicationUtils;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.servlet.ServletContext;
import javax.sql.DataSource;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.FileFilterUtils;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.ReloadableDefinitionsFactory;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.entando.entando.aps.system.init.AbstractInitializerManager;
import org.entando.entando.aps.system.init.ComponentManager;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.aps.system.init.InstallationReportDAO;
import org.entando.entando.aps.system.init.model.ComponentInstallationReport;
import org.entando.entando.aps.system.init.model.SystemInstallationReport;
import org.entando.entando.plugins.jpcomponentinstaller.aps.TextProviderSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author M.Casari
 */
public class DefaultComponentInstaller extends AbstractInitializerManager implements IComponentInstaller, ApplicationContextAware {

    private static final Logger _logger = LoggerFactory.getLogger(DefaultComponentInstaller.class);

    @Override
    public void install(AvailableArtifact availableArtifact, String version, InputStream is) throws ApsSystemException {
        try {
            if (availableArtifact.getType() == AvailableArtifact.Type.PLUGIN) {
                this.installPlugin(availableArtifact, version, is);
            } else if (availableArtifact.getType() == AvailableArtifact.Type.BUNDLE) {
                this.installBundle(availableArtifact, version, is);
            } else if (availableArtifact.getType() == AvailableArtifact.Type.APP) {
                this.installApp(availableArtifact, version, is);
            } else {
                throw new ApsSystemException("Wrong artifact type value");
            }
        } catch (Exception e) {
            throw new ApsSystemException("Unexpected error during artifact installation", e);
        } finally {
            try {
                ServletContext servletContext = ((ConfigurableWebApplicationContext) this._applicationContext).getServletContext();
                ApsWebApplicationUtils.executeSystemRefresh(servletContext);
            } catch (Throwable t) {
                _logger.error("Error while reloading configuration", t);
                throw new ApsSystemException("Error while reloading configuration", t);
            }
        }
    }

    private void installPlugin(AvailableArtifact availableArtifact, String version, InputStream is) throws Exception {
        ServletContext servletContext = ((ConfigurableWebApplicationContext) this._applicationContext).getServletContext();
        String filename = availableArtifact.getGroupId() + "_" + availableArtifact.getArtifactId() + "_" + version + ".war";
        String artifactName = availableArtifact.getArtifactId();
        String appRootPath = servletContext.getRealPath("/");
        File destDir = new File(appRootPath);
        String tempDirPath = appRootPath + "componentinstaller" + File.separator + artifactName;
        File artifactFile = new File(appRootPath + "componentinstaller" + File.separator + filename);

        FileUtils.copyInputStreamToFile(is, artifactFile);
        this.extractArchiveFile(artifactFile, tempDirPath);
        File artifactFileRootDir = new File(artifactFile.getParentFile().getAbsolutePath()
                + File.separator + artifactName);

        List<File> tilesFiles = (List<File>) FileUtils.listFiles(artifactFileRootDir, FileFilterUtils.suffixFileFilter("-tiles.xml"), FileFilterUtils.trueFileFilter());
        if (tilesFiles == null) {
            tilesFiles = new ArrayList<File>();
        }

        File tempArtifactRootDir = this.extractAllJars(destDir, artifactName);
        List<File> pluginSqlFiles = (List<File>) FileUtils.listFiles(tempArtifactRootDir, new String[]{"sql"}, true);
        List<File> pluginXmlFiles = (List<File>) FileUtils.listFiles(tempArtifactRootDir, new String[]{"xml"}, true);

        List<File> mainAppJarLibraries = (List<File>) FileUtils.listFiles(new File(destDir.getAbsolutePath() + File.separator + "WEB-INF" + File.separator + "lib"), new String[]{"jar"}, true);
        List<File> pluginJarLibraries = (List<File>) FileUtils.listFiles(artifactFileRootDir, new String[]{"jar"}, true);
        pluginJarLibraries = filterOutDuplicateLibraries(mainAppJarLibraries, pluginJarLibraries);

        FileUtils.copyDirectory(artifactFileRootDir, destDir);

        List<File> allFiles = new ArrayList<File>();
        File systemParamsFile = FileUtils.getFile(destDir + File.separator + "WEB-INF" + File.separator + "conf", "systemParams.properties");
        allFiles.add(systemParamsFile);
        allFiles.addAll(pluginJarLibraries);
        allFiles.addAll(pluginSqlFiles);
        allFiles.addAll(pluginXmlFiles);
        //The classloader's content is stored in servlet context and 
        //updated every time so the classloader will eventually contain 
        //the classes and resources of all the installed plugins
        List<URL> urlList = (List<URL>) servletContext.getAttribute("pluginInstallerURLList");
        if (urlList == null) {
            urlList = new ArrayList<URL>();
            servletContext.setAttribute("pluginInstallerURLList", urlList);
        }
        Set<File> jarSet = (Set<File>) servletContext.getAttribute("pluginInstallerJarSet");
        if (jarSet == null) {
            jarSet = new HashSet<File>();
            servletContext.setAttribute("pluginInstallerJarSet", jarSet);
        }
        jarSet.addAll(pluginJarLibraries);
        URLClassLoader cl = getURLClassLoader(urlList, allFiles.toArray(new File[0]), servletContext);
        loadClasses(jarSet.toArray(new File[0]), cl);
        servletContext.setAttribute("componentInstallerClassLoader", cl);
        ComponentManager.setComponentInstallerClassLoader(cl);

        //load plugin and dependencies contexts 
        File componentFile = this.getFileFromArtifactJar(tempArtifactRootDir, "component.xml");
        String componentToInstallName = this.getComponentName(componentFile);

        InitializerManager initializerManager = (InitializerManager) this._applicationContext.getBean("InitializerManager");
        initializerManager.reloadCurrentReport();
        SystemInstallationReport currentReport = initializerManager.getCurrentReport();
        if (null != currentReport.getComponentReport(componentToInstallName, false)) {
            currentReport.removeComponentReport(componentToInstallName);
            this.saveReport(currentReport);
            initializerManager.reloadCurrentReport();
        }

        List<String> components = this.getAllComponents(artifactFileRootDir);

        Properties properties = new Properties();
        properties.load(new FileInputStream(systemParamsFile));

        for (String componentName : components) {
            List<String> configLocs = new ArrayList<String>();
            InitializerManager currenInitializerManager = (InitializerManager) _applicationContext.getBean("InitializerManager");
            currentReport = currenInitializerManager.getCurrentReport();
            ComponentInstallationReport cir = currentReport.getComponentReport(componentName, false);
            if (null != cir && cir.getStatus().equals(SystemInstallationReport.Status.UNINSTALLED)) {
                currentReport.removeComponentReport(componentName);
                this.saveReport(currentReport);
                currenInitializerManager.reloadCurrentReport();
            }

            configLocs = this.getConfigPaths(artifactFileRootDir, componentName);
            if (configLocs.isEmpty()) {
                continue;
            }
            
            ClassPathXmlApplicationContext newContext = (ClassPathXmlApplicationContext) loadContext((String[]) configLocs.toArray(new String[0]), cl, componentName, properties);

            this.reloadActionsDefinitions(newContext);
            this.reloadResourcsBundles(newContext, servletContext);
            TilesContainer container = TilesAccess.getContainer(servletContext);
            this.reloadTilesDefinitions(tilesFiles, container);

            currenInitializerManager.reloadCurrentReport();
            ComponentManager componentManager = (ComponentManager) _applicationContext.getBean("ComponentManager");
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(newContext.getClassLoader());
                componentManager.refresh();
            } catch (Exception e) {
                throw e;
            } finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
        }
    }

    private void installBundle(AvailableArtifact availableArtifact, String version, InputStream is) throws Exception {
        this.installPlugin(availableArtifact, version, is);
    }

    private void installApp(AvailableArtifact availableArtifact, String version, InputStream is) throws Exception {
        this.installPlugin(availableArtifact, version, is);
    }

    private File extractAllJars(File destDir, String artifactId) throws Exception {
        List<File> files = (List<File>) FileUtils.listFiles(new File(destDir.getAbsolutePath() + File.separator + "componentinstaller" + File.separator + artifactId), new String[]{"jar"}, true);
        File tempDir = null;
        for (File jarFile : files) {
            if (!jarFile.getName().toLowerCase().contains("entando")) {
                continue;
            }
            if (jarFile.getName().contains(artifactId)) {
                tempDir = new File(destDir.getAbsolutePath() + File.separator + "componentinstaller" + File.separator + artifactId + "-extractedJars" + File.separator + jarFile.getName().replaceAll(".jar", ""));
            }
            extractArchiveFile(jarFile, destDir.getAbsolutePath() + File.separator + "componentinstaller" + File.separator + artifactId + "-extractedJars" + File.separator + jarFile.getName().replaceAll(".jar", ""));
        }
        return tempDir;
    }

    private List<String> getConfigPaths(File artifactDir, String componentName) throws SAXException, ParserConfigurationException, IOException {
        List<String> configLocs = new ArrayList<String>();
        List<File> files = (List<File>) FileUtils.listFiles(new File(artifactDir + "-extractedJars"), new String[]{"xml"}, true);
        for (File xmlFile : files) {
            if (xmlFile.getName().equalsIgnoreCase("component.xml")) {
                String compName = this.getComponentName(xmlFile);
                if (compName.equals(componentName)) {
                    if (isPlugin(xmlFile)) {
                        configLocs.add("classpath*:spring/plugins/" + compName + "/aps/**/**.xml");
                        configLocs.add("classpath*:spring/plugins/" + compName + "/apsadmin/**/**.xml");
                        List<String> components = this.getDependencies(xmlFile);
                        for (String component : components) {
                            configLocs.add("classpath*:spring/plugins/" + component + "/aps/**/**.xml");
                            configLocs.add("classpath*:spring/plugins/" + component + "/apsadmin/**/**.xml");
                        }
                    }
                }
            }
        }
        return configLocs;
    }

    private List<String> getAllComponents(File artifactDir) throws SAXException, ParserConfigurationException, IOException {
        Set<String> components = new HashSet<String>();
        List<File> files = (List<File>) FileUtils.listFiles(new File(artifactDir + "-extractedJars"), new String[]{"xml"}, true);
        for (File xmlFile : files) {
            if (xmlFile.getName().equalsIgnoreCase("component.xml")) {
                String compName = this.getComponentName(xmlFile);
                components.add(compName);
                List<String> dependencies = this.getDependencies(xmlFile);
                for (String component : dependencies) {
                    components.add(component);
                }                            
            }
        }
        List<String> componentsList = new ArrayList<String>();
        componentsList.addAll(components);
        return componentsList;
    }

    private List<File> filterOutDuplicateLibraries(List<File> mainAppJarLibraries, List<File> pluginJarLibraries) throws Exception {
        List<File> filesToRemove = new ArrayList<File>();
        for (File file : pluginJarLibraries) {
            String fileName = file.getName();
            if (!filesToRemove.contains(file) && fileName.contains("struts2")) {
                filesToRemove.add(file);
            }
        }
        pluginJarLibraries.removeAll(filesToRemove);
        return pluginJarLibraries;
    }

    private void reloadActionsDefinitions(ClassPathXmlApplicationContext context) throws Exception {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(context.getClassLoader());
            Dispatcher.getInstance().getConfigurationManager().reload();
        } catch (Exception e) {
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }

    private void reloadResourcsBundles(ClassPathXmlApplicationContext context, ServletContext servletContext) throws Exception {
        TextProviderSupport.servletContext = servletContext;
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(context.getClassLoader());
            LocalizedTextUtil.setDelegatedClassLoader(context.getClassLoader());
            addPluginRecourceBundle(servletContext);
        } catch (Exception e) {
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }

    private void reloadTilesDefinitions(List<File> tilesFiles, TilesContainer container) throws MalformedURLException, DefinitionsFactoryException {
        if (container instanceof BasicTilesContainer) {
            BasicTilesContainer basic = (BasicTilesContainer) container;
            DefinitionsFactory factory = basic.getDefinitionsFactory();
            if (factory instanceof ReloadableDefinitionsFactory) {
                ReloadableDefinitionsFactory rFactory = (ReloadableDefinitionsFactory) factory;
                for (File file : tilesFiles) {
                    URL source = new URL("file://localhost/" + file.getAbsolutePath());
                    ((UrlDefinitionsFactory) rFactory).addSource(source);
                    rFactory.refresh();
                }
            }
        }
    }

    private void addPluginRecourceBundle(ServletContext servletContext) {
        Set<String> jaredPlugins = new HashSet<String>();
        Set<String> classPlugins = new HashSet<String>();
        classPlugins = discoverClasses(TOMCAT_CLASSES, servletContext);
        jaredPlugins = discoverJars(TOMCAT_LIB, servletContext);
        Iterator<String> itr = classPlugins.iterator();
        while (itr.hasNext()) {
            String cur = itr.next();
            _logger.debug("Trying to load resources under development @ {}", cur);
            LocalizedTextUtil.addDefaultResourceBundle(cur + this.PLUGIN_RESOURCE_NAME);
        }
        itr = jaredPlugins.iterator();
        while (itr.hasNext()) {
            String cur = itr.next();
            _logger.debug("Trying to load resources @{}", cur);
            LocalizedTextUtil.addDefaultResourceBundle(cur + this.PLUGIN_RESOURCE_NAME);
        }
        _logger.info("EntandoPluginLabelListener summary: {} plugin detected ({} under development)", (classPlugins.size() + jaredPlugins.size()), classPlugins.size());
    }

    /**
     * Discover the directories holding plugins within the classpath.
     *
     * @param path the path where to start the search from
     * @param servletContext the servlet context event
     */
    private Set<String> discoverClasses(String path, ServletContext servletContext) {
        Set<String> plugins = new HashSet<String>();

        Set<String> directory = servletContext.getResourcePaths(path);
        if (null != directory && !directory.isEmpty()) {
            Iterator<String> itr = directory.iterator();
            while (itr.hasNext()) {
                String currentDirectory = itr.next();
                boolean skip = false;
                // AVOID USELESS LOOPS IF POSSIBLE
                Iterator<String> exclude = _plugin_exclusion_directories.iterator();
                while (exclude.hasNext()) {
                    String currentDirectoryExcluded = exclude.next();
                    if (currentDirectory.contains(currentDirectoryExcluded)
                            && !currentDirectory.contains(PLUGIN_DIRECTORY)) {
                        skip = true;
                        break;
                    }
                }
                if (skip) {
                    continue;
                }
                if (currentDirectory.contains(PLUGIN_DIRECTORY)
                        && currentDirectory.endsWith(PLUGIN_APSADMIN_PATH)) {
                    currentDirectory = currentDirectory.replaceFirst(TOMCAT_CLASSES, "");
                    plugins.add(currentDirectory);
                } else {
                    plugins.addAll(discoverClasses(currentDirectory, servletContext));
                }
            }
        }
        return plugins;
    }

    private Set<String> discoverJars(String path, ServletContext servletContext) {
        Set<String> plugins = new HashSet<String>();
        Set<String> directory = servletContext.getResourcePaths(path);
        Iterator<String> itr = directory.iterator();
        // ITERATE OVER PATHS
        while (null != itr && itr.hasNext()) {
            String currentJar = itr.next();
            InputStream is = servletContext.getResourceAsStream(currentJar);
            plugins.addAll(discoverJarPlugin(is));
        }
        return plugins;
    }

    /**
     * Fetch all the entries in the given (jar) input stream and look for the
     * plugin directory.
     *
     * @param is the input stream to analyse
     */
    private Set<String> discoverJarPlugin(InputStream is) {
        Set<String> plugins = new HashSet<String>();
        if (null == is) {
            return plugins;
        }
        JarEntry je = null;
        try {
            JarInputStream jis = new JarInputStream(is);
            do {
                je = jis.getNextJarEntry();
                if (null != je) {
                    String URL = je.toString();
                    if (URL.contains(PLUGIN_DIRECTORY)
                            && URL.endsWith(PLUGIN_APSADMIN_PATH)) {
                        plugins.add(URL);
                    }
                }
            } while (je != null);
        } catch (Throwable t) {
            _logger.error("error in discoverJarPlugin", t);
            //ApsSystemUtils.logThrowable(t, this, "discoverJarPlugin");
        }
        return plugins;
    }

    private void loadClasses(File[] jarFiles, URLClassLoader cl) throws Exception {
        for (File input : jarFiles) {
            try {
                //load classes from plugin's jar files using the classloader above
                //loadClassesFromJar(input, cl);
                JarFile jarFile = new JarFile(input.getAbsolutePath());
                Enumeration e = jarFile.entries();
                while (e.hasMoreElements()) {
                    JarEntry je = (JarEntry) e.nextElement();
                    if (je.isDirectory() || !je.getName().endsWith(".class")) {
                        continue;
                    }
                    String className = je.getName().substring(0, je.getName().length() - 6);
                    className = className.replace('/', '.');
                    try {
                        cl.loadClass(className);
                    } catch (Throwable ex) {
                        String error = "Error loadin class: " + className;
                        _logger.error(error);
                    }
                }
            } catch (Throwable e) {
                String error = "Unexpected error loading class for file: " + input.getName() + " - " + e.getMessage();
                _logger.error(error, e);
                throw new Exception(error, e);
            }
        }
    }

    private ApplicationContext loadContext(String[] configLocations, URLClassLoader cl, String contextDisplayName, Properties properties) throws Exception {
        ServletContext servletContext = ((ConfigurableWebApplicationContext) this._applicationContext).getServletContext();
        //if plugin's classes have been loaded we can go on
        List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
        if (ctxList == null) {
            ctxList = new ArrayList<ClassPathXmlApplicationContext>();
            servletContext.setAttribute("pluginsContextsList", ctxList);
        }
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        ClassPathXmlApplicationContext newContext = null;
        try {
            //create a new spring context with the classloader and the paths defined as parameter in pluginsInstallerContext.xml.
            //The new context is given the default webapplication context as its parent  
            Thread.currentThread().setContextClassLoader(cl);
            newContext = new ClassPathXmlApplicationContext();
            //ClassPathXmlApplicationContext newContext = new ClassPathXmlApplicationContext(configLocations, applicationContext);    
            PropertyPlaceholderConfigurer configurer = new PropertyPlaceholderConfigurer();
            configurer.setProperties(properties);
            newContext.addBeanFactoryPostProcessor(configurer);
            newContext.setClassLoader(cl);
            newContext.setParent(this._applicationContext);
            String[] configLocs = new String[]{"classpath:spring/restServerConfig.xml",
                "classpath:spring/baseSystemConfig.xml"};
            newContext.setConfigLocations(configLocs);
            newContext.refresh();
            BaseConfigManager baseConfigManager = (BaseConfigManager) ((ConfigurableWebApplicationContext) this._applicationContext).getBean("BaseConfigManager");
            baseConfigManager.init();
            newContext.setConfigLocations(configLocations);
            newContext.refresh();
            newContext.setDisplayName(contextDisplayName);
            ClassPathXmlApplicationContext currentCtx = (ClassPathXmlApplicationContext) this.getStoredContext(contextDisplayName);
            if (currentCtx != null) {
                currentCtx.close();
                ctxList.remove(currentCtx);
            }
            ctxList.add(newContext);

        } catch (Exception e) {
            _logger.error("Unexpected error loading application context: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
        return newContext;
    }

    private ApplicationContext getStoredContext(String contextDisplayName) {
        ServletContext servletContext = ((ConfigurableWebApplicationContext) _applicationContext).getServletContext();
        List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
        for (ClassPathXmlApplicationContext ctx : ctxList) {
            if (contextDisplayName.equals(ctx.getDisplayName())) {
                return ctx;
            }
        }
        return null;
    }

    private URLClassLoader getURLClassLoader(List<URL> urlList, File[] files, ServletContext servletContext) {
        for (File input : files) {
            try {
                if (!urlList.contains(input.toURI().toURL())) {
                    urlList.add(input.toURI().toURL());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        URL[] urls = new URL[urlList.size()];
        for (int i = 0; i < urlList.size(); i++) {
            URL url = urlList.get(i);
            urls[i] = url;
        }

        URLClassLoader cl = URLClassLoader.newInstance(urls, Thread.currentThread().getContextClassLoader());
        return cl;
    }

    private void extractArchiveFile(File file, String destinationPath) throws ZipException, FileNotFoundException, IOException {
        ZipFile zipFile = new ZipFile(file);
        Enumeration<? extends ZipEntry> entries = zipFile.entries();
        File entryDestination = null;
        while (entries.hasMoreElements()) {
            ZipEntry entry = entries.nextElement();
            entryDestination = new File(destinationPath, entry.getName());
            entryDestination.getParentFile().mkdirs();
            if (entry.isDirectory()) {
                entryDestination.mkdirs();
            } else {
                InputStream in = zipFile.getInputStream(entry);
                OutputStream out = new FileOutputStream(entryDestination);
                IOUtils.copy(in, out);
                IOUtils.closeQuietly(in);
                IOUtils.closeQuietly(out);
            }
        }
    }

    private File extractArtifactJar(File rootDir, String artifactId) throws ZipException, IOException {
        List<File> files = (List<File>) FileUtils.listFiles(rootDir, new String[]{"jar"}, true);
        File tempDir = null;
        for (File jarFile : files) {
            if (jarFile.getName().contains(artifactId)) {
                tempDir = new File(rootDir + File.separator + "componentinstaller" + File.separator + artifactId + "-extractedJars" + File.separator + jarFile.getName().replaceAll(".jar", ""));
                extractArchiveFile(jarFile, rootDir + File.separator + "componentinstaller" + File.separator + artifactId + "-extractedJars" + File.separator + jarFile.getName().replaceAll(".jar", ""));
            }
        }
        return tempDir;
    }

    private File getFileFromArtifactJar(File rootDir, String fileName) throws ZipException, IOException {
        File resultFile = null;
        List<File> files = (List<File>) FileUtils.listFiles(rootDir, new String[]{"xml"}, true);
        for (File xmlFile : files) {
            if (xmlFile.getName().equalsIgnoreCase(fileName)) {
                resultFile = xmlFile;
            }
        }
        return resultFile;
    }

    private List<String> getDependencies(File file) throws SAXException, ParserConfigurationException, IOException {
        List<String> dependencies = new ArrayList<String>();
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        Node depNode = null;
        if (doc.getElementsByTagName("dependencies") != null) {
            depNode = doc.getElementsByTagName("dependencies").item(0);
            if (depNode != null) {
                NodeList nList = depNode.getChildNodes();
                for (int temp = 0; temp < nList.getLength(); temp++) {
                    Node nNode = nList.item(temp);
                    if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                        Element eElement = (Element) nNode;
                        dependencies.add(eElement.getTextContent());
                    }
                }
            }
        }
        return dependencies;
    }

    private boolean isPlugin(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        String code = null;
        Node codeNode = doc.getElementsByTagName("artifactId").item(0);
        if (codeNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) codeNode;
            code = eElement.getTextContent();
            if (code.contains("plugin")) {
                return true;
            }
        }
        return false;
    }

    private String getComponentName(File file) throws SAXException, ParserConfigurationException, IOException {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(file);
        doc.getDocumentElement().normalize();
        String code = null;
        Node codeNode = doc.getElementsByTagName("code").item(0);
        if (codeNode.getNodeType() == Node.ELEMENT_NODE) {
            Element eElement = (Element) codeNode;
            code = eElement.getTextContent();
        }
        return code;
    }

    private void saveReport(SystemInstallationReport report) throws BeansException {
        if (null == report || report.getReports().isEmpty()) {
            return;
        }
        try {
            InstallationReportDAO dao = new InstallationReportDAO();
            DataSource dataSource = (DataSource) this.getBeanFactory().getBean("portDataSource");
            dao.setDataSource(dataSource);
            dao.saveConfigItem(report.toXml(), "production"/*this.getConfigVersion()*/);
        } catch (Throwable t) {
            _logger.error("Error saving report", t);
            throw new FatalBeanException("Error saving report", t);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        this._applicationContext = ac;
    }

    private ApplicationContext _applicationContext;

    /**
     * This contains all the directories to exclude from the recursive search
     * when PLUGIN_DIRECTORY does NOT exist in the URL or path
     */
    private final List<String> _plugin_exclusion_directories = Arrays.asList("/test/",
            "/aps/", "/apsadmin/");

    /**
     * Path within the plugin where the global properties are stored.
     */
    private final String PLUGIN_APSADMIN_PATH = "/apsadmin/";

    /**
     * Path to the global properties file within the plugin package
     */
    private final String PLUGIN_RESOURCE_NAME = "global-messages";

    /**
     * This is the directory where plugins are searched
     */
    private final String PLUGIN_DIRECTORY = "plugins";

    /**
     * The URL of Tomcat classes
     */
    private final String TOMCAT_CLASSES = "/WEB-INF/classes/";

    /**
     * The URL of the Tomcat shared lib directory
     */
    private final String TOMCAT_LIB = "/WEB-INF/lib/";

}
