(ns work-tasks.scripts.api
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.scripts.apiHelpers :as apiHelpers]))

;GENERAL API FUNCTIONS
(defn update-tasks-in-store []
  "General function call after updateing the store to trigger a re-render"
  (.then (.getItem (.-localforage js/window) "tasks") (fn [tasks]
                                                    (handle-state-change {:type "add-task" :value (js->clj tasks :keywordize-keys true)}))))

;TASKS API CONCERNS
(defn get-tasks []
  "Gets all the current tasks"
  (.then (.getItem (.-localforage js/window) "tasks")
    (fn [tasks]
      (js->clj tasks :keywordize-keys true))))

(defn handle-save-new-task [task]
  "Saves a new task"
  (.then (get-tasks)
    (fn [tasks]
      (.then (.setItem (.-localforage js/window) "tasks" (clj->js (conj tasks (apiHelpers/add-metadata task)))
        (fn [tasks]
          (update-tasks-in-store))))))) ; TODO add alert

(defn handle-save-edit-task [task]
  "edits an existing task"
  (.then (get-tasks)
    (fn [tasks]
      (.then (.setItem (.-localforage js/window) "tasks" (clj->js
                        (map (fn [savedTask] ; updates only the task taht we passed in
                          (if (= (:id savedTask) (:id task))
                            task
                            savedTask)) tasks))
        (fn [tasks]
          (update-tasks-in-store))))))) ;TODO add alert

(defmulti save-task (fn [task] (boolean (:id task))))
  (defmethod save-task true
    [task]
    (handle-save-edit-task task))
  (defmethod save-task false
    [task]
    (handle-save-new-task task))

(defn delete-task [taskId]
  "deletes an existing task")


;LABEL API CONCERNS
(defn update-label [labelId]
  "Updates a given label")