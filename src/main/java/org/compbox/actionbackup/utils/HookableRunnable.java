/*
 * Copyright 2016 Kevin Raoofi.
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
package org.compbox.actionbackup.utils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Kevin Raoofi
 */
public class HookableRunnable implements Runnable {

    private final Deque<Runnable> dq = new LinkedList<>();

    public HookableRunnable(Runnable r) {
        dq.add(r);
    }

    public void addPre(Runnable r) {
        dq.addFirst(r);
    }

    public void addPost(Runnable r) {
        dq.addLast(r);
    }

    @Override
    public void run() {
        for (Runnable r : dq) {
            r.run();
        }
    }

    public static class HookableRunnableFactory {

        public static final HookableRunnableFactory DEFAULT
                = new HookableRunnableFactory(
                        Collections.EMPTY_LIST, Collections.EMPTY_LIST);

        private final Collection<Runnable> pre;
        private final Collection<Runnable> post;

        public HookableRunnableFactory(Collection<Runnable> pre,
                Collection<Runnable> post) {
            this.pre = pre;
            this.post = post;
        }

        public HookableRunnableFactory combine(HookableRunnableFactory hrf) {
            final List<Runnable> lPre = new ArrayList<>();
            final List<Runnable> lPost = new ArrayList<>();

            pre.addAll(this.pre);
            pre.addAll(hrf.pre);

            post.addAll(this.post);
            post.addAll(hrf.post);

            return new HookableRunnableFactory(lPre, lPost);
        }

        public HookableRunnable createInstance(Runnable runnable) {
            HookableRunnable res
                    = new HookableRunnable(runnable);

            for (Runnable r : pre) {
                res.addPre(r);
            }

            for (Runnable r : post) {
                res.addPost(r);
            }

            return res;
        }

    }
}
