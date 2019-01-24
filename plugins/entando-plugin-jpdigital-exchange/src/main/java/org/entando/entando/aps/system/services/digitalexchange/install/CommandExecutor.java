/*
 * Copyright 2019-Present Entando Inc. (http://www.entando.com) All rights reserved.
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */
package org.entando.entando.aps.system.services.digitalexchange.install;

import org.entando.entando.aps.system.services.digitalexchange.install.model.Command;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.bind.JAXB;
import org.entando.entando.aps.system.services.digitalexchange.install.model.Commands;
import org.entando.entando.web.common.model.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

/**
 * Executes operations defined in the component.xml using SpEL. The purpose is
 * to call an arbitrary API controller (even one that is not a dependency of the
 * DE plugin itself, like CMS controllers). It is also able to interpret some
 * default commands using the #call custom function. This simplify the
 * definition of the most common operations inside the component.xml. See the
 * file commands.xml.
 */
@Component
public class CommandExecutor implements ApplicationContextAware {

    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    private final ExpressionParser parser = new SpelExpressionParser();
    private final StandardEvaluationContext context = new StandardEvaluationContext();

    private final Commands commands;

    public CommandExecutor() {

        try {
            context.registerFunction("fromXml", JAXB.class.getDeclaredMethod("unmarshal", InputStream.class, Class.class));
            context.registerFunction("fromJson", CommandUtils.class.getDeclaredMethod("fromJson", InputStream.class, Class.class));
            context.registerFunction("bindingResult", CommandUtils.class.getDeclaredMethod("getBindingResult"));
            context.registerFunction("call", Command.class.getDeclaredMethod("newInstance", String.class, String[].class));
        } catch (NoSuchMethodException ex) {
            throw new BeanInitializationException("Unable to register SpEL functions", ex);
        }

        try (InputStream in = getClass().getClassLoader().getResourceAsStream("component/plugins/digitalExchange/commands.xml")) {
            commands = JAXB.unmarshal(in, Commands.class);
        } catch (IOException ex) {
            throw new BeanInitializationException("Unable to load installation commands", ex);
        }
    }

    public void execute(String expression) {
        Expression expr = parser.parseExpression(expression);
        Object result = expr.getValue(context);
        if (result instanceof Command) {
            handleCommand((Command) result);
        } else if (result instanceof ResponseEntity) {
            checkControllerResponse(expression, (ResponseEntity) result);
        }
    }

    private void handleCommand(Command command) {
        String templateExpression = commands.getExpression(command.getName());
        if (templateExpression == null) {
            throw new InstallationException("Unrecognized command " + command.getName());
        }
        execute(String.format(templateExpression, (Object[]) command.getParameters()));
    }

    private void checkControllerResponse(String expression, ResponseEntity<?> response) {
        if (!response.getStatusCode().is2xxSuccessful()) {
            if (response.getBody() instanceof RestResponse) {
                RestResponse<?, ?> restResponse = (RestResponse) response.getBody();
                restResponse.getErrors().forEach(error -> logger.error(error.getMessage()));
            }
            throw new InstallationException("Execution of " + expression + " returned " + response.getStatusCodeValue());
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context.setBeanResolver(new BeanFactoryResolver(applicationContext));
    }
}
