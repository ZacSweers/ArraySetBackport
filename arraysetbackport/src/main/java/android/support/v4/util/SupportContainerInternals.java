package android.support.v4.util;

/**
 * Exposes package-local helpers from the support lib
 */
public final class SupportContainerInternals {
    public static final int[] EMPTY_INTS = ContainerHelpers.EMPTY_INTS;
    public static final long[] EMPTY_LONGS = ContainerHelpers.EMPTY_LONGS;
    public static final Object[] EMPTY_OBJECTS = ContainerHelpers.EMPTY_OBJECTS;

    public static int idealIntArraySize(int need) {
        return ContainerHelpers.idealIntArraySize(need);
    }

    public static int idealLongArraySize(int need) {
        return ContainerHelpers.idealLongArraySize(need);
    }

    public static int idealByteArraySize(int need) {
        return ContainerHelpers.idealByteArraySize(need);
    }

    public static boolean equal(Object a, Object b) {
        return ContainerHelpers.equal(a, b);
    }

    // This is Arrays.binarySearch(), but doesn't do any argument validation.
    public static int binarySearch(int[] array, int size, int value) {
        return ContainerHelpers.binarySearch(array, size, value);
    }

    public static int binarySearch(long[] array, int size, long value) {
        return ContainerHelpers.binarySearch(array, size, value);
    }

    public abstract static class MapCollections<K, V> extends android.support.v4.util.MapCollections<K, V> { }
}
