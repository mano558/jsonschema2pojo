/**
 * Copyright © 2010-2014 Nokia
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

package org.jsonschema2pojo.util;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class InflectorTest {

    @Test
    public void testSingularize() {

        assertThat(Inflector.getInstance().singularize("dwarves"), is("dwarf"));
        assertThat(Inflector.getInstance().singularize("curves"), is("curve"));
        assertThat(Inflector.getInstance().singularize("halves"), is("half"));
        assertThat(Inflector.getInstance().singularize("vertices"), is("vertex"));
        assertThat(Inflector.getInstance().singularize("proofs"), is("proof"));
        assertThat(Inflector.getInstance().singularize("moths"), is("moth"));
        assertThat(Inflector.getInstance().singularize("houses"), is("house"));
        assertThat(Inflector.getInstance().singularize("rooves"), is("roof"));
        assertThat(Inflector.getInstance().singularize("elves"), is("elf"));
        assertThat(Inflector.getInstance().singularize("baths"), is("bath"));
        assertThat(Inflector.getInstance().singularize("leaves"), is("leaf"));
        assertThat(Inflector.getInstance().singularize("calves"), is("calf"));
        assertThat(Inflector.getInstance().singularize("lives"), is("life"));
        assertThat(Inflector.getInstance().singularize("knives"), is("knife"));
        assertThat(Inflector.getInstance().singularize("addresses"), is("address"));
        assertThat(Inflector.getInstance().singularize("matresses"), is("matress"));

        assertThat(Inflector.getInstance().singularize("bison"), is("bison"));
        assertThat(Inflector.getInstance().singularize("buffalo"), is("buffalo"));
        assertThat(Inflector.getInstance().singularize("deer"), is("deer"));
        assertThat(Inflector.getInstance().singularize("fish"), is("fish"));
        assertThat(Inflector.getInstance().singularize("sheep"), is("sheep"));
        assertThat(Inflector.getInstance().singularize("squid"), is("squid"));
        assertThat(Inflector.getInstance().singularize("matress"), is("matress"));
        assertThat(Inflector.getInstance().singularize("address"), is("address"));

        assertThat(Inflector.getInstance().singularize("s"), is("s"));

        assertThat(Inflector.getInstance().pluralize("matress"), is("matresses"));
        assertThat(Inflector.getInstance().pluralize("address"), is("addresses"));

    }

    @Test
    public void testThreadSafety() throws InterruptedException, ExecutionException {
        final int numberOfThreads = 10;
        final int numberOfTasks = 1000;

        class SingulariseSomething implements Callable<String> {
            private final String something;

            public SingulariseSomething(String something) {
                this.something = something;
            }

            @Override
            public String call() throws Exception {
                return Inflector.getInstance().singularize(something);
            }
        }

        List<SingulariseSomething> tasks = new ArrayList<SingulariseSomething>();
        for (int i = 0; i < numberOfTasks; i++) {
            tasks.add(new SingulariseSomething(i + "zebras"));
        }

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<Future<String>> futures = executorService.invokeAll(tasks);

        for (Future<String> future : futures) {
            future.get();
        }
    }

}