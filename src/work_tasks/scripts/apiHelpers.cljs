(ns work-tasks.scripts.apiHelpers
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

(defn determine-id-unique [tasks id]
  "parses current tasks to determine if ID is unique"
    (if (= (filter tasks (fn [task] (= (:id task) id))) 0)
      true
      false))

(defn generate-unique-id []
  "generates a unique Id"
  (str (random-uuid)))

(defn add-metadata [task]
  "Adds the extra metadata we want to track on each item"
  (conj task {:id (generate-unique-id) :completed? false :created (js/Date.)}))

(defn get-completed-tasks [taskVector]
  "Takes all tasks and returns the completed ones"
  (filter taskVector (fn [task] (boolean (:completed? task)))))

(defn filter-completed-tasks [taskVector]
  "Takes all tasks and returns ones NOT completed"
  (filter taskVector (fn [task] (not (boolean (:completed? task))))))

(defn filter-deleted-tasks [task taskId]
  "takes a taskID for delete and reurns all others"
  (boolean (not= (:id task) taskId)))