ArraySetBackport
=========

Backport of the [ArraySet](arrayset) implementation from Android Lollipop. Functionally, it's
nearly identical to the original code, with some minor tweaks to use the same structure that 
[ArrayMap](arraymap) uses to support older version. This should be treated as a mirror, and not 
necessarily a unique implementation.

My hope is that Google will eventually put ArraySet in the support library like they did with 
ArrayMap. Until then, I'm using this.

Usage
-----

*(From the AOSP documentation)*

ArraySet is a generic set data structure that is designed to be more memory efficient than a 
traditional [HashSet](hashet). The design is very similar to [ArrayMap](arraymap), with all 
of the caveats described there. This implementation is separate from ArrayMap, however, so the 
Object array contains only one item for each entry in the set (instead of a pair for a mapping).

Note that this implementation is not intended to be appropriate for data structures that may 
contain large numbers of items. It is generally slower than a traditional HashSet, since lookups 
require a binary search and adds and removes require inserting and deleting entries in the array. 
For containers holding up to hundreds of items, the performance difference is not significant, 
less than 50%.

Because this container is intended to better balance memory use, unlike most other standard 
Java containers it will shrink its array as items are removed from it. Currently you have no 
control over this shrinking -- if you set a capacity and then remove an item, it may reduce 
the capacity to better match the current size. In the future an explicit call to set the 
capacity should turn off this aggressive shrinking behavior.

Download
--------

```groovy
compile 'io.sweers:arraysetbackport:0.1.0'
```

Snapshots of the development version are available in [Sonatype's snapshots repository](snapshots).

License
-------

    Copyright (C) 2016 Zac Sweers

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

 [arrayset]: http://developer.android.com/reference/android/util/ArraySet.html
 [hashset]: http://developer.android.com/reference/java/util/HashSet.html
 [arraymap]: http://developer.android.com/reference/android/util/ArrayMap.html
 [snapshots]: https://oss.sonatype.org/content/repositories/snapshots/
