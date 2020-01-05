package com.fri.code.users.services.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("configuration-properties")
public class IntegrationConfiguration {
    @ConfigValue(value = "code-subjects.enabled", watch = true)
    private boolean subjectsServiceEnabled;

    public boolean isSubjectsServiceEnabled() {
        return subjectsServiceEnabled;
    }

    public void setSubjectsServiceEnabled(boolean subjectsServiceEnabled) {
        this.subjectsServiceEnabled = subjectsServiceEnabled;
    }
}
