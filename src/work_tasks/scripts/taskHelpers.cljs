(ns work-tasks.scripts.taskHelpers)

(defn get-completed-tasks [taskVector]
  "Takes all tasks and returns the completed ones"
  (filter (fn [task] (boolean (:completed? task))) taskVector))

(defn filter-completed-tasks [taskVector]
  "Takes all tasks and returns ones NOT completed"
  (filter (fn [task] (not (boolean (:completed? task)))) taskVector))

; TODO watch performance on this as it will likely be n^2 used in conjunction with our date creator
; i.e each date we need to generate then check all tasks against it?
; Might be easier to pass it a map and itterate through the tasks once and assign them to the map correctly
; This will do for now
(defn get-tasks-due-on-date [date tasks]
  "Takes in a moment.js date formatted MMDDYYY and returns the tasks that are due on that date"
  (filter (fn [task] (= (:due task) date)) tasks))