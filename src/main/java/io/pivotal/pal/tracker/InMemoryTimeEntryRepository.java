package io.pivotal.pal.tracker;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

public class InMemoryTimeEntryRepository implements TimeEntryRepository {

    private HashMap<Long, TimeEntry> myList = new HashMap<Long,TimeEntry>();
    long id = 1L;

    public TimeEntry create(TimeEntry timeEntry) {
        timeEntry.setId(id);
        myList.put(id,timeEntry);
        id++;
        return timeEntry;
    }

    public TimeEntry find(long id) {
        return myList.get(id);
    }

    public TimeEntry update(long id, TimeEntry timeEntry) {
        timeEntry.setId(id);
        myList.put(id,timeEntry);
        return timeEntry;
    }

    public void delete(long id) {
        myList.remove(id);
    }

    public List<TimeEntry> list() {
        Collection<TimeEntry> values = myList.values();

        List<TimeEntry> list = new ArrayList<>();
        for (TimeEntry entry : values) {
            list.add(entry);
        }
        return list;
    }
}
