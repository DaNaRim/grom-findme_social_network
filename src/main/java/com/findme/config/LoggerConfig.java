package com.findme.config;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.ConfigurationFactory;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Order;
import org.apache.logging.log4j.core.config.builder.api.AppenderComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.ConfigurationBuilder;
import org.apache.logging.log4j.core.config.builder.api.LayoutComponentBuilder;
import org.apache.logging.log4j.core.config.builder.api.RootLoggerComponentBuilder;
import org.apache.logging.log4j.core.config.builder.impl.BuiltConfiguration;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import java.net.URI;

@Plugin(name = "CustomConfigurationFactory", category = ConfigurationFactory.CATEGORY)
@Order(50)
public class LoggerConfig extends ConfigurationFactory {

    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);

        LayoutComponentBuilder standard = builder.newLayout("PatternLayout");
        standard.addAttribute("pattern", "[%d] %level %c{1}: %m");

        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        console.add(standard);
        builder.add(console);

        AppenderComponentBuilder file = builder.newAppender("log", "File");
        file.addAttribute("fileName", "target/logging.log");
        file.add(standard);
        builder.add(file);

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.ERROR);
        rootLogger.add(builder.newAppenderRef("stdout"));
        rootLogger.add(builder.newAppenderRef("log"));
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

/*
    static Configuration createConfiguration(final String name, ConfigurationBuilder<BuiltConfiguration> builder) {
        builder.setConfigurationName(name);

        LayoutComponentBuilder standard = builder.newLayout("PatternLayout");
        standard.addAttribute("pattern", "[%d] %level %c{1}: %m");

        AppenderComponentBuilder console = builder.newAppender("stdout", "Console");
        console.add(standard);

//        FilterComponentBuilder flow = builder.newFilter("MarkerFilter",
//                Filter.Result.ACCEPT, Filter.Result.DENY);
//        flow.addAttribute("marker", "FLOW");
//        console.add(flow);

        builder.add(console);

        AppenderComponentBuilder file = builder.newAppender("log", "File");
        file.addAttribute("fileName", "target/logging.log");
        file.add(standard);
        builder.add(file);

//        AppenderComponentBuilder rolling = builder.newAppender("rolling", "RollingFile");
//        rolling.addAttribute("fileName", "rolling.log");
//        rolling.addAttribute("filePattern", "rolling-%d{MM-dd-yy}.log.gz");
//
//        ComponentBuilder triggeringPolicies = builder.newComponent("Policies")
//                .addComponent(builder.newComponent("CronTriggeringPolicy")
//                        .addAttribute("schedule", "0 0 0 * * ?"))
//                .addComponent(builder.newComponent("SizeBasedTriggeringPolicy")
//                        .addAttribute("size", "100M"));
//        rolling.addComponent(triggeringPolicies);
//
//        builder.add(rolling);

        RootLoggerComponentBuilder rootLogger = builder.newRootLogger(Level.ERROR);
        rootLogger.add(builder.newAppenderRef("stdout"));
        rootLogger.add(builder.newAppenderRef("log"));
//        rootLogger.add(builder.newAppenderRef("rolling"));
        builder.add(rootLogger);

        return builder.build();
    }
 */