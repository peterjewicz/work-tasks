(ns work-tasks.services.state.dispatcher
  (:require [work-tasks.services.state.state :refer [app-state update-active-view ]]
            [work-tasks.services.state.labelState :refer [update-labels]]
            [work-tasks.services.state.taskState :refer [update-tasks-list update-active-task]]
            [work-tasks.services.state.notificationState :refer [update-notification-state]]))

; As we need more mutations for state we can add them here - Handle state change
; calls the correct method based on the type passed in
(defmulti handle-state-change (fn [action] (:type action)))
  (defmethod handle-state-change "update-active-view"
    [action]
    (update-active-view app-state (:value action)))
  (defmethod handle-state-change "add-task"
    [action]
    (update-tasks-list app-state (:value action)))
  (defmethod handle-state-change "add-labels"
    [action]
    (update-labels app-state (:value action)))
  (defmethod handle-state-change "update-labels"
    [action]
    (update-labels app-state (:value action)))
  (defmethod handle-state-change "update-active-task"
    [action]
    (update-active-task app-state (:value action)))
  (defmethod handle-state-change "update-notification-state"
    [action]
    (update-notification-state app-state (:value action)))