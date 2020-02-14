package io.squant.app.resource;

import java.util.List;

public class Page<T> {

    private final List<T> data;
    private final int index;
    private final int count;

    public Page(List<T> data, int index, int count) {
        this.data = data;
        this.index = index;
        this.count = count;
    }

    public List<T> getData() {
        return data;
    }

    public int getIndex() {
        return index;
    }

    public int getCount() {
        return count;
    }
}
