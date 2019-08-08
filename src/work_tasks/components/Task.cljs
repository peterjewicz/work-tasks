(ns work-tasks.components.task
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

(defn dispatch-for-edit-view [task]
  "Handles calling the two functions to setup the edited view"
  (handle-state-change {:type "update-active-task" :value task})
  (handle-state-change {:type "update-active-view" :value "task"}))


(defn Task [task]
  [:div.HomeTask {:key (:id task) :on-click #(dispatch-for-edit-view task)}
    [:div
      [:h2 (:title task)]
      [:p (:due task)]]
    [:p (:details task)]])