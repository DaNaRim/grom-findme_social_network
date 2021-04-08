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

    private static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);
        builder.setMonitorInterval("5");

        AppenderComponentBuilder console = builder.newAppender("stdout", "Console")
                .addAttribute("target", "SYSTEM_ERR")
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "%d [%t] %-5p %c - %m%n %throwable{short}%n"));
        builder.add(console);

        AppenderComponentBuilder rolling = builder.newAppender("rolling", "RollingFile")
                .addAttribute("fileName", "C:/Users/user/IdeaProjects/grom-findme_social_network/logs/rolling.log")
                .addAttribute("filePattern", "C:/Users/user/IdeaProjects/grom-findme_social_network/logs/rolling-%d{yy-MM-dd}-%i.log.gz")
                .add(builder.newLayout("PatternLayout")
                        .addAttribute("pattern", "%d [%t] %-5p %c - %m%n %throwable%n"))
                .addComponent(builder.newComponent("Policies")
                        .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
                                .addAttribute("size", "1 MB"))
                        .addComponent(builder.newComponent("TimeBasedTriggeringPolicy")
                                .addAttribute("interval", "1")))
                .addComponent(builder.newComponent("DefaultRolloverStrategy")
                        .addAttribute("max", 5));
        builder.add(rolling);

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.WARN)
                .add(builder.newAppenderRef("stdout"))
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
