/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mycorp;

import com.google.ratel.RatelConfig;
import com.google.ratel.service.template.*;

/**
 *
 */
public class ExampleRatelConfig extends RatelConfig {

    @Override
    protected TemplateService createTemplateService() {
        return new VelocityTemplateService();
    }

    
}
