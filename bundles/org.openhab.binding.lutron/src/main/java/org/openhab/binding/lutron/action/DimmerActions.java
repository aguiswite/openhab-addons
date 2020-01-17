/**
 * Copyright (c) 2010-2020 Contributors to the openHAB project
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 */
package org.openhab.binding.lutron.action;

import static org.openhab.binding.lutron.internal.LutronBindingConstants.CHANNEL_LIGHTLEVEL;

import org.eclipse.jdt.annotation.NonNullByDefault;
import org.eclipse.jdt.annotation.Nullable;
import org.eclipse.smarthome.core.library.types.DecimalType;
import org.eclipse.smarthome.core.thing.binding.ThingActions;
import org.eclipse.smarthome.core.thing.binding.ThingActionsScope;
import org.eclipse.smarthome.core.thing.binding.ThingHandler;
import org.eclipse.smarthome.core.types.Command;
import org.openhab.binding.lutron.internal.handler.DimmerHandler;
import org.openhab.core.automation.annotation.ActionInput;
import org.openhab.core.automation.annotation.RuleAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DimmerActions} defines thing actions for DimmerHandler.
 *
 * @author Bob Adair - Initial contribution
 */
@ThingActionsScope(name = "lutron")
@NonNullByDefault
public class DimmerActions implements ThingActions {
    private final static Logger logger = LoggerFactory.getLogger(DimmerActions.class);

    private @Nullable DimmerHandler handler;

    public DimmerActions() {
        logger.trace("Lutron Dimmer actions service created");
    }

    // TODO: activate() and deactivate() methods

    @Override
    public void setThingHandler(@Nullable ThingHandler handler) {
        if (handler instanceof DimmerHandler) {
            this.handler = (DimmerHandler) handler;
        }
    }

    @Override
    public @Nullable ThingHandler getThingHandler() {
        return handler;
    }

    @RuleAction(label = "setLightLevel", description = "Send light level command with fade and delay times")
    public void setLightLevel(
            @ActionInput(name = "command", label = "command", description = "Command") @Nullable Command command,
            @ActionInput(name = "fadeTime", label = "fadeTime", description = "Fade time") @Nullable DecimalType fadeTime,
            @ActionInput(name = "delayTime", label = "delayTime", description = "Delay time") @Nullable DecimalType delayTime) {
        DimmerHandler dimmerHandler = handler;
        if (dimmerHandler == null) {
            logger.warn("Handler not set for Dimmer thing actions.");
            return;
        }

        if (command == null) {
            logger.debug("Ignoring setLightLevel command due to null value.");
            return;
        }
        if (fadeTime == null) {
            logger.debug("Ignoring setLightLevel command '{}' due to null value for fade time.", command);
            return;
        }
        if (delayTime == null) {
            logger.debug("Ignoring setLightLevel command '{}' due to null value for delay time.", command);
            return;
        }

        dimmerHandler.handleCommand(CHANNEL_LIGHTLEVEL, command, fadeTime.intValue(), delayTime.intValue());
    }

    // Static method for Rules DSL backward compatibility
    public static void setLightLevel(@Nullable ThingActions actions, @Nullable Command command,
            @Nullable DecimalType fadeTime, @Nullable DecimalType delayTime) {
        if (actions instanceof DimmerActions) {
            ((DimmerActions) actions).setLightLevel(command, fadeTime, delayTime);
        } else {
            throw new IllegalArgumentException("Instance is not a DimmerActions class.");
        }
    }

}
