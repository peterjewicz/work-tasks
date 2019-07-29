(ns work-tasks.scripts.taskHelpers)

(defn get-completed-tasks [taskVector]
  "Takes all tasks and returns the completed ones"
  (filter (fn [task] (boolean (:completed? task))) taskVector))

(defn filter-completed-tasks [taskVector]
  "Takes all tasks and returns ones NOT completed"
  (filter (fn [task] (not (boolean (:completed? task)))) taskVector))