package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@ResponseBody
public class TimeEntryController {

    private final TimeEntryRepository timeEntryRepository;
    private final CounterService counter;
    private final GaugeService gauge;

    public TimeEntryController(TimeEntryRepository timeEntryRepository, CounterService counter, GaugeService gauge){
        this.timeEntryRepository = timeEntryRepository;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping("/time-entries")
    public ResponseEntity create(@RequestBody TimeEntry timeEntryToCreate) {
        TimeEntry createdTimeEntry = timeEntryRepository.create(timeEntryToCreate);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createdTimeEntry);
    }

    @GetMapping("/time-entries/{timeEntryId}")
    public ResponseEntity read(@PathVariable long timeEntryId) {
        TimeEntry found = timeEntryRepository.find(timeEntryId);
        if(found != null){
            counter.increment("TimeEntry.read");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(found);
        }
        else
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> list = timeEntryRepository.list();
        counter.increment("TimeEntry.listed");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(list);
    }

    @PutMapping("/time-entries/{timeEntryId}")
    public ResponseEntity update(@PathVariable long timeEntryId,@RequestBody TimeEntry expected) {
        TimeEntry update = timeEntryRepository.update(timeEntryId,expected);
        if(update != null){
            counter.increment("TimeEntry.updated");
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(update);
        }
        else
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(null);
    }

    @DeleteMapping("/time-entries/{timeEntryId}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long timeEntryId) {
        timeEntryRepository.delete(timeEntryId);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntry.count", timeEntryRepository.list().size());
        return ResponseEntity
                .status(HttpStatus. NO_CONTENT)
                .body(null);
    }
}
