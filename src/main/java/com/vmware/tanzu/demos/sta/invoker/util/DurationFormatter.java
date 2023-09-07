/*
 * Copyright (c) 2023 VMware, Inc. or its affiliates
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vmware.tanzu.demos.sta.invoker.util;

import org.springframework.stereotype.Component;

import java.text.NumberFormat;
import java.time.Duration;
import java.util.Locale;

@Component
public class DurationFormatter {
    public String format(Duration d) {
        if (d == null) {
            return null;
        }
        final NumberFormat nf = NumberFormat.getIntegerInstance(Locale.US);
        final long ms = d.toMillis();
        if (ms < 1000) {
            return String.format("%s ms", nf.format(ms));
        }
        final long seconds = d.toSeconds();
        if (seconds == 1) {
            return "1 second";
        }
        return String.format("%s seconds", nf.format(seconds));
    }
}
