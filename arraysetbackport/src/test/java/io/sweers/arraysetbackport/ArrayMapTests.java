/*
 * Copyright (C) 2013 The Android Open Source Project
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

package io.sweers.arraysetbackport;

import android.util.Log;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.fail;

/**
 * Test suite from AOSP for ArrayMap and ArraySet. Adjusted to run with Robolectric and ArrayMap
 * tests removed.
 * <p>
 * From https://github.com/android/platform_frameworks_base/blob/master/tests/ActivityTests/src/com/google/android/test/activity/ArrayMapTests.java
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21)
public class ArrayMapTests {
    static final int OP_ADD = 1;
    static final int OP_REM = 2;

    static int[] OPS = new int[]{
            OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD,
            OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD,
            OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM,
            OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM,

            OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD,
            OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM,

            OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD,
            OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM,

            OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD,
            OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM,

            OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD,
            OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD, OP_ADD,
            OP_ADD, OP_ADD, OP_ADD,
            OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM,
            OP_REM, OP_REM, OP_REM,
            OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM, OP_REM,
    };

    static int[] KEYS = new int[]{
            // General adding and removing.
            -1, 1900, 600, 200, 1200, 1500, 1800, 100, 1900,
            2100, 300, 800, 600, 1100, 1300, 2000, 1000, 1400,
            600, -1, 1900, 600, 300, 2100, 200, 800, 800,
            1800, 1500, 1300, 1100, 2000, 1400, 1000, 1200, 1900,

            // Shrink when removing item from end.
            100, 200, 300, 400, 500, 600, 700, 800, 900,
            900, 800, 700, 600, 500, 400, 300, 200, 100,

            // Shrink when removing item from middle.
            100, 200, 300, 400, 500, 600, 700, 800, 900,
            900, 800, 700, 600, 500, 400, 200, 300, 100,

            // Shrink when removing item from front.
            100, 200, 300, 400, 500, 600, 700, 800, 900,
            900, 800, 700, 600, 500, 400, 100, 200, 300,

            // Test hash collisions.
            105, 106, 108, 104, 102, 102, 107, 5, 205,
            4, 202, 203, 3, 5, 101, 109, 200, 201,
            0, -1, 100,
            106, 108, 104, 102, 103, 105, 107, 101, 109,
            -1, 100, 0,
            4, 5, 3, 5, 200, 203, 202, 201, 205,
    };

    static class ControlledHash {
        final int mValue;

        ControlledHash(int value) {
            mValue = value;
        }

        @Override
        public final boolean equals(Object o) {
            return !(o == null || !(o instanceof ControlledHash)) && mValue == ((ControlledHash) o).mValue;
        }

        @Override
        public final int hashCode() {
            return mValue / 100;
        }

        @Override
        public final String toString() {
            return Integer.toString(mValue);
        }
    }

    private static boolean compare(Object v1, Object v2) {
        if (v1 == null) {
            return v2 == null;
        }
        return v2 != null && v1.equals(v2);
    }

    private static boolean compareSets(HashSet set, ArraySet array) {
        if (set.size() != array.size()) {
            fail("Bad size: expected " + set.size() + ", got " + array.size());
            return false;
        }

        for (Object entry : set) {
            if (!array.contains(entry)) {
                fail("Bad value: expected " + entry + " not found in ArraySet");
                return false;
            }
        }

        for (int i = 0; i < array.size(); i++) {
            Object entry = array.valueAt(i);
            if (!set.contains(entry)) {
                fail("Bad value: unexpected " + entry + " in ArraySet");
                return false;
            }
        }

        int index = 0;
        for (Object entry : array) {
            Object realEntry = array.valueAt(index);
            if (!compare(entry, realEntry)) {
                fail("Bad iterator: expected value " + realEntry + ", got " + entry
                        + " at index " + index);
                return false;
            }
            index++;
        }

        return true;
    }

    private static void dump(Set set, ArraySet array) {
        Log.i("test", "HashSet of " + set.size() + " entries:");
        for (Object entry : set) {
            Log.i("test", "    " + entry);
        }
        Log.i("test", "ArraySet of " + array.size() + " entries:");
        for (int i = 0; i < array.size(); i++) {
            Log.i("test", "    " + array.valueAt(i));
        }
    }

    @Before
    public void setUp() throws Exception {
        // Route robolectric logs to sout
        ShadowLog.stream = System.out;
    }

    @Test
    public void run() {
        HashSet<ControlledHash> hashSet = new HashSet<>();
        ArraySet<ControlledHash> arraySet = new ArraySet<>();

        for (int i = 0; i < OPS.length; i++) {
            boolean hashChanged;
            boolean arrayChanged;
            ControlledHash key = KEYS[i] < 0 ? null : new ControlledHash(KEYS[i]);
            switch (OPS[i]) {
                case OP_ADD:
                    Log.i("test", "Adding key: " + KEYS[i]);
                    hashChanged = hashSet.add(key);
                    arrayChanged = arraySet.add(key);
                    break;
                case OP_REM:
                    Log.i("test", "Removing key: " + KEYS[i]);
                    hashChanged = hashSet.remove(key);
                    arrayChanged = arraySet.remove(key);
                    break;
                default:
                    fail("Bad operation " + OPS[i] + " @ " + i);
                    return;
            }
            if (hashChanged != arrayChanged) {
                dump(hashSet, arraySet);
                fail("Bad change: expected " + hashChanged + ", got " + arrayChanged);
            }
            if (!compareSets(hashSet, arraySet)) {
                dump(hashSet, arraySet);
                fail();
            }
        }

        ControlledHash lookup = new ControlledHash(50000);
        arraySet.add(new ControlledHash(50000));
        Iterator<ControlledHash> it = arraySet.iterator();
        while (it.hasNext()) {
            if (it.next().equals(lookup)) {
                it.remove();
            }
        }
        if (arraySet.contains(lookup)) {
            dump(hashSet, arraySet);
            fail("Bad set iterator: didn't remove test key");
        }

        if (!equalsSetTest()) {
            fail();
        }

        // set copy constructor test
        ArraySet<Integer> newSet = new ArraySet<>();
        for (int i = 0; i < 10; ++i) {
            newSet.add(i);
        }
        ArraySet<Integer> setCopy = new ArraySet<>(newSet);
        if (!compare(setCopy, newSet)) {
            dump(newSet, setCopy);
            fail("ArraySet copy constructor failure: expected " +
                    newSet + ", got " + setCopy);
        }

        Log.i("test", "Test successful; printing final set.");
        dump(hashSet, arraySet);
    }

    private static boolean equalsSetTest() {
        ArraySet<Integer> set1 = new ArraySet<>();
        ArraySet<Integer> set2 = new ArraySet<>();
        HashSet<Integer> set3 = new HashSet<>();
        if (!compare(set1, set2) || !compare(set1, set3) || !compare(set3, set2)) {
            fail("ArraySet equals failure for empty sets " + set1 + ", " +
                    set2 + ", " + set3);
            return false;
        }

        for (int i = 0; i < 10; ++i) {
            set1.add(i);
            set2.add(i);
            set3.add(i);
        }
        if (!compare(set1, set2) || !compare(set1, set3) || !compare(set3, set2)) {
            fail("ArraySet equals failure for populated sets " + set1 + ", " +
                    set2 + ", " + set3);
            return false;
        }

        set1.remove(0);
        if (compare(set1, set2) || compare(set1, set3) || compare(set3, set1)) {
            fail("ArraSet equals failure for set size " + set1 + ", " +
                    set2 + ", " + set3);
            return false;
        }

        set1.add(-1);
        if (compare(set1, set2) || compare(set1, set3) || compare(set3, set1)) {
            fail("ArraySet equals failure for set contents " + set1 + ", " +
                    set2 + ", " + set3);
            return false;
        }

        return true;
    }
}
