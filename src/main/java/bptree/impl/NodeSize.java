package bptree.impl;

import bptree.PageProxyCursor;
import org.neo4j.io.pagecache.PagedFile;

import java.io.IOException;

/**
 * Created by max on 5/8/15.
 */
public class NodeSize {

    public static PageProxyCursor cursor;

    //public static boolean leafNodeContainsSpaceForNewKey(long nodeId, long[] newKey){
//        return leafNodeByteSize(nodeId, newKey) < DiskCache.PAGE_SIZE;
  //  }

    public static boolean leafNodeContainsSpaceForNewKey(PageProxyCursor cursor, long[] newKey){
        return leafNodeByteSize(cursor, newKey) < DiskCache.PAGE_SIZE;
    }

    public static int leafNodeByteSize(IndexTree tree, long nodeId, long[] newKey){
        int size = 0;
        try (PageProxyCursor cursor = tree.disk.getCursor(nodeId, PagedFile.PF_EXCLUSIVE_LOCK)) {
                    size = leafNodeByteSize(cursor, newKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static int leafNodeByteSize(PageProxyCursor cursor, long[] newKey){
        int byteSize = 0;
        int numberOfKeys = NodeHeader.getNumberOfKeys(cursor);
        byteSize += NodeHeader.NODE_HEADER_LENGTH;

        int keyLength = NodeHeader.getKeyLength(cursor);
        byteSize += ((numberOfKeys + 1) * keyLength * 8);

        return byteSize;
    }

    public static boolean internalNodeContainsSpaceForNewKeyAndChild(PageProxyCursor cursor, long[] newKey){
        return internalNodeByteSize(cursor, newKey) < DiskCache.PAGE_SIZE;
    }

    public static int internalNodeByteSize(IndexTree tree, long nodeId, long[] newKey){
        int size = 0;
        try (PageProxyCursor cursor = tree.disk.getCursor(nodeId, PagedFile.PF_EXCLUSIVE_LOCK)) {
                    size = internalNodeByteSize(cursor, newKey);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return size;
    }

    public static int internalNodeByteSize(PageProxyCursor cursor, long[] newKey){
        int byteSize = 0;
        int numberOfKeys = NodeHeader.getNumberOfKeys(cursor);
        byteSize += NodeHeader.NODE_HEADER_LENGTH;
        byteSize += (numberOfKeys + 2) * 8; //calculate number of children;

        int keyLength = NodeHeader.getKeyLength(cursor);
        byteSize += ((numberOfKeys + 1) * keyLength * 8);

        return byteSize;
    }
}
