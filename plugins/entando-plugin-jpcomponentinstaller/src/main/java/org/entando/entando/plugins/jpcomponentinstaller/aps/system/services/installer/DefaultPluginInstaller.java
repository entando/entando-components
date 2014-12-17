/*
 *
 * Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 * This file is part of Entando software.
 * Entando is a free software;
 * You can redistribute it and/or modify it
 * under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
 * 
 * See the file License for the specific language governing permissions   
 * and limitations under the License
 * 
 * 
 * 
 * Copyright 2014 Entando S.r.l. (http://www.entando.com) All rights reserved.
 *
 */
package org.entando.entando.plugins.jpcomponentinstaller.aps.system.services.installer;

import com.agiletec.aps.system.SystemConstants;
import com.agiletec.aps.system.exception.ApsSystemException;
import com.agiletec.aps.system.services.baseconfig.BaseConfigManager;
import com.opensymphony.xwork2.util.LocalizedTextUtil;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.instrument.Instrumentation;
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
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import javax.servlet.ServletContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.struts2.dispatcher.Dispatcher;
import org.apache.tiles.TilesContainer;
import org.apache.tiles.access.TilesAccess;
import org.apache.tiles.definition.DefinitionsFactory;
import org.apache.tiles.definition.DefinitionsFactoryException;
import org.apache.tiles.definition.ReloadableDefinitionsFactory;
import org.apache.tiles.definition.UrlDefinitionsFactory;
import org.apache.tiles.impl.BasicTilesContainer;
import org.entando.entando.aps.system.init.InitializerManager;
import org.entando.entando.apsadmin.common.UserAvatarAction;
import org.entando.entando.plugins.jpcomponentinstaller.aps.TextProviderSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
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
 * @author mcasari
 */
public class DefaultPluginInstaller implements IPluginInstaller, ApplicationContextAware {

    private static final Logger _logger = LoggerFactory.getLogger(UserAvatarAction.class);

    private ApplicationContext applicationContext;

    private String artifactName;

    @Override
    public void install(AvailableArtifact availableArtifact, String version, InputStream is) throws ApsSystemException {
        String filename = availableArtifact.getGroupId() + "_" + availableArtifact.getArtifactId() + "_" + version + ".war";
        //TODO PARLARE CON MARIO
        //IL FILE WAR contiene anche le dipendenze, non c'è bisogno di scaricare anche quelle...
        System.out.println("*************INSTALL****************");
        System.out.println("filename " + filename);
        System.out.println("type " + availableArtifact.getType());
        System.out.println("version " + version);
        System.out.println("*****************************");
        try {
            if (availableArtifact.getType() == AvailableArtifact.Type.PLUGIN) {
                this.installPlugin(availableArtifact, version, is);
            } else if (availableArtifact.getType() == AvailableArtifact.Type.BUNDLE) {
                this.installBundle(availableArtifact, version, is);
            } else if (availableArtifact.getType() == AvailableArtifact.Type.APP) {
                //type app (currently not handled)
            } else {
                throw new ApsSystemException("Wrong artifact type value");
            }
        } catch (Exception e) {
            throw new ApsSystemException("Unexpected error during artifact installation", e);
        }
    }

    private void installPlugin(AvailableArtifact availableArtifact, String version, InputStream is) throws Exception {

        ServletContext servletContext = ((ConfigurableWebApplicationContext) applicationContext).getServletContext();

        String filename = availableArtifact.getGroupId() + "_" + availableArtifact.getArtifactId() + "_" + version + ".war";
        artifactName = availableArtifact.getArtifactId().split("-")[2];

        String appRootPath = servletContext.getRealPath("/");
        File destDir = new File(appRootPath);
        String tempDirPath = appRootPath + "temp" + File.separator + artifactName;
        File artifactFile = new File(appRootPath + "temp" + File.separator + filename);

        FileUtils.copyInputStreamToFile(is, artifactFile);
        this.extractArchiveFile(artifactFile, tempDirPath);
        File artifactFileRootDir = new File(artifactFile.getParentFile().getAbsolutePath()
                + File.separator + artifactName);

        List<String> configLocs = new ArrayList<String>();
        List<File> tilesFiles = new ArrayList<File>();
        File tilesFile = this.getFileFromDir(artifactFileRootDir, artifactName + "-tiles.xml", new String[]{"xml"});
        if (tilesFile != null) {
            tilesFiles.add(tilesFile);
        }

        File tempArtifactRootDir = this.extractArtifactJar(destDir, artifactName);
        List<File> pluginSqlFiles = (List<File>) FileUtils.listFiles(tempArtifactRootDir, new String[]{"sql"}, true);
        List<File> pluginXmlFiles = (List<File>) FileUtils.listFiles(tempArtifactRootDir, new String[]{"xml"}, true);
        File componentFile = this.getFileFromArtifactJar(tempArtifactRootDir, "component.xml");
        fillConfPathsFromDependencies(componentFile, configLocs, tilesFiles, destDir);

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
        URLClassLoader cl = getURLClassLoader(allFiles.toArray(new File[0]), servletContext);
        loadClasses((File[]) pluginJarLibraries.toArray(new File[0]), cl);
        configLocs.add("classpath*:spring/plugins/jacms/aps/**/**.xml");
        configLocs.add("classpath*:spring/plugins/jacms/apsadmin/**/**.xml");
        configLocs.add("classpath*:spring/plugins/" + artifactName + "/aps/**/**.xml");
        configLocs.add("classpath*:spring/plugins/" + artifactName + "/apsadmin/**/**.xml");
        //load plugin context 
        Properties properties = new Properties();
        properties.load(new FileInputStream(systemParamsFile));
        ClassPathXmlApplicationContext newContext = (ClassPathXmlApplicationContext) loadContext((String[]) configLocs.toArray(new String[0]), cl, artifactName, properties);

        this.reloadActionsDefinitions(newContext);
        this.reloadResourcsBundles(newContext, servletContext);

        TilesContainer container = TilesAccess.getContainer(servletContext);
        this.reloadTilesDefinitions(tilesFiles, container);
    }

    private void installBundle(AvailableArtifact availableArtifact, String version, InputStream is) throws Exception {
        ServletContext servletContext = ((ConfigurableWebApplicationContext) applicationContext).getServletContext();

        String filename = availableArtifact.getGroupId() + "_" + availableArtifact.getArtifactId() + "_" + version + ".war";
        String artifactName = availableArtifact.getArtifactId().split("-")[2];

        String appRootPath = servletContext.getRealPath("/");
        File destDir = new File(appRootPath);
        String tempDirPath = appRootPath + "temp" + File.separator + artifactName;
        File artifactFile = new File(appRootPath + "temp" + File.separator + filename);

        FileUtils.copyInputStreamToFile(is, artifactFile);
        this.extractArchiveFile(artifactFile, tempDirPath);
        File artifactFileRootDir = new File(artifactFile.getParentFile().getAbsolutePath()
                + File.separator + artifactName);
        FileUtils.copyDirectory(artifactFileRootDir, destDir);

        InitializerManager initializerManager = (InitializerManager) applicationContext.getBean("InitializerManager");
        initializerManager.init();
        BaseConfigManager baseConfigManager = (BaseConfigManager) ((ConfigurableWebApplicationContext) applicationContext).getBean(SystemConstants.BASE_CONFIG_MANAGER);
        baseConfigManager.init();
    }

    private void fillConfPathsFromDependencies(File componentFile, List<String> configLocs, List<File> tilesFiles, File destDir) throws Exception {
        List<String> dependencies = this.getDependencies(componentFile);
        for (String depencencyName : dependencies) {
            configLocs.add("classpath*:spring/plugins/" + depencencyName + "/aps/**/**.xml");
            configLocs.add("classpath*:spring/plugins/" + depencencyName + "/apsadmin/**/**.xml");
            File tilesFile = this.getFileFromDir(destDir, depencencyName + "-tiles.xml", new String[]{"xml"});
            if (tilesFile != null) {
                tilesFiles.add(tilesFile);
            }
        }
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
        _logger.info("JapsPluginLabelListener summary: {} plugin detected ({} under development)", (classPlugins.size() + jaredPlugins.size()), classPlugins.size());
    }

    /**
     * Discover the directories holding plugins within the classpath
     *
     * @param path the path where to start the search from
     * @param event the servlet context event
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
                _logger.error(error);
                throw new Exception(error, e);
            }
        }
    }

    private ApplicationContext loadContext(String[] configLocations, URLClassLoader cl, String contextDisplayName, Properties properties) throws Exception {
        ServletContext servletContext = ((ConfigurableWebApplicationContext) applicationContext).getServletContext();

        //if plugin's classes have been loaded we can go on
        List<ClassPathXmlApplicationContext> ctxList = (List<ClassPathXmlApplicationContext>) servletContext.getAttribute("pluginsContextsList");
        if (ctxList == null) {
            ctxList = new ArrayList<ClassPathXmlApplicationContext>();
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
            newContext.setParent(applicationContext);
            String[] configLocs = new String[]{"classpath:spring/restServerConfig.xml",
                "classpath:spring/baseSystemConfig.xml"};
            newContext.setConfigLocations(configLocs);
            newContext.refresh();
            BaseConfigManager baseConfigManager = (BaseConfigManager) ((ConfigurableWebApplicationContext) applicationContext).getBean("BaseConfigManager");
            baseConfigManager.init();
            newContext.setConfigLocations(configLocations);
            newContext.refresh();
            newContext.setDisplayName(contextDisplayName);
            ctxList.add(newContext);
            servletContext.setAttribute("pluginsContextsList", ctxList);
        } catch (Exception e) {
            _logger.error("Unexpected error loading application context: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
        return newContext;
    }

    private URLClassLoader getURLClassLoader(File[] files, ServletContext servletContext) {
        List<URL> urlList = (List<URL>) servletContext.getAttribute("pluginInstallerURLList");
        if (urlList == null) {
            urlList = new ArrayList<URL>();
            servletContext.setAttribute("pluginInstallerURLList", urlList);
        }
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
                tempDir = new File(rootDir + File.separator + "temp" + File.separator + jarFile.getName().replaceAll(".jar", "") + "-temp");
                extractArchiveFile(jarFile, rootDir + File.separator + "temp" + File.separator + jarFile.getName().replaceAll(".jar", "") + "-temp");
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

    private File getFileFromDir(File rootDir, String fileName, String[] extensions) throws ZipException, IOException {
        File resultFile = null;
        List<File> files = (List<File>) FileUtils.listFiles(rootDir, extensions, true);
        for (File xmlFile : files) {
            if (xmlFile.getName().equalsIgnoreCase(fileName)) {
                resultFile = xmlFile;
            }
        }
        return resultFile;
    }

    @Override
    public void setApplicationContext(ApplicationContext ac) throws BeansException {
        applicationContext = ac;
    }

    /**
     * This contains all the directories to exclude from the recursive search
     * when PLUGIN_DIRECTORY does NOT exist in the URL or path
     */
    private List<String> _plugin_exclusion_directories = Arrays.asList("/test/",
            "/aps/",
            "/apsadmin/");

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
    private String TOMCAT_CLASSES = "/WEB-INF/classes/";

    /**
     * The URL of the Tomcat shared lib directory
     */
    private String TOMCAT_LIB = "/WEB-INF/lib/";

}
