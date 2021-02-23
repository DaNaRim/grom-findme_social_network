package com.findme.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LoggerConfig extends ConfigurationFactory {

    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);

//        LayoutComponentBuilder standard = builder.newLayout("PatternLayout")
//                .addAttribute("pattern", "[%d] %level %c{1}: %m%n");

        AppenderComponentBuilder console = builder.newAppender("stdout", "Console")
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "%d [%t] %-5p %c - %m%n"));
        builder.add(console);

        AppenderComponentBuilder file = builder.newAppender("log", "File")
                .addAttribute("fileName", "C:/Users/Назар/IdeaProjects/findme/logs/logging.log")
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "%d [%t] %-5p %c - %m%n"));
        builder.add(file);

        AppenderComponentBuilder rolling = builder.newAppender("rolling", "RollingFile")
                .addAttribute("fileName", "C:/Users/Назар/IdeaProjects/findme/logs/rolling.log")
                .addAttribute("filePattern", "rolling-%d{MM-dd-yy}.log.gz")
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "%d [%t] %-5p %c - %m%n"))
                .addComponent(builder.newComponent("Policies")
                        .addComponent(builder.newComponent("CronTriggeringPolicy")
                                .addAttribute("schedule", "0 0 0 * * ?"))
                        .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                                .addAttribute("size", "2M")));
        builder.add(rolling);


        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.ERROR)
                .add(builder.newAppenderRef("stdout"))
                .add(builder.newAppenderRef("log"))
                .add(builder.newAppenderRef("rolling"));
        builder.add(rootLogger);

        return builder.build();
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final ConfigurationSource source) {
        return getConfiguration(loggerContext, source.toString(), null);
    }

    @Override
    public Configuration getConfiguration(final LoggerContext loggerContext, final String name, final URI configLocation) {
        ConfigurationBuilder<BuiltConfiguration> builder = newConfigurationBuilder();
        return createConfiguration(name, builder);
    }

    @Override
    protected String[] getSupportedTypes() {
        return new String[]{"*"};
    }
}
