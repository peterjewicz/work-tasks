(ns work-tasks.scripts.api
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.scripts.apiHelpers :as apiHelpers]))

;GENERAL API FUNCTIONS
(defn get-data-by-type [type]
  "Quries an entity from out localstorage and adds it to local store"
  (.then (.getItem (.-localforage js/window) type) (fn [tasks]
                                                    (handle-state-change {:type "add-task" :value (js->clj tasks :keywordize-keys true)}))))

;TASKS API CONCERNS
(defn get-tasks []
  "Gets all the current tasks"
  (.then (.getItem (.-localforage js/window) "tasks")
    (fn [tasks]
      tasks)))

(defn save-new-task [task]
  "Saves a new task"
  (.then (get-tasks)
    (fn [tasks]
      (.then (.setItem (.-localforage js/window) "tasks" (clj->js (conj tasks (apiHelpers/add-metadata @task)))
        (fn [value]
          ;handle state update
        ))))))

(defn edit-task [task]
  "edits an existing task")

(defn delete-task [taskId]
  "deletes an existing task")


;LABEL API CONCERNS
(defn update-label [labelId]
  "Updates a given label")