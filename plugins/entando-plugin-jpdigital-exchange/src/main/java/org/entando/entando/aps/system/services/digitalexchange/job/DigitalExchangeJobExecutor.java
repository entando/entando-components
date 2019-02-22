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

package org.entando.entando.aps.system.services.digitalexchange.job;

import java.util.function.Consumer;
import org.entando.entando.aps.system.jpa.servdb.DigitalExchangeJob;

public interface DigitalExchangeJobExecutor {

    void execute(DigitalExchangeJob job, Consumer<DigitalExchangeJob> consumer) throws JobExecutionException;
}
