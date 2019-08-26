(ns work-tasks.components.task
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

(defn dispatch-for-edit-view [task]
  "Handles calling the two functions to setup the edited view"
  (handle-state-change {:type "update-active-task" :value task})
  (handle-state-change {:type "update-active-view" :value "task"}))

(defn generate-label-display [taskLabels labels]
  "Maps list of ints to their corresponding label details"
  (if labels
    (map (fn [singleLabel]
          (first (filter (fn [label]
            (= (:id label) singleLabel)) labels))
          ) taskLabels)
    () ))

(defn Task [task labels]
  (let [labelDisplay (generate-label-display (:labels task) labels)]
    [:div.HomeTask {:key (:id task) :on-click #(dispatch-for-edit-view task)}
      [:div
        [:h2 (:title task)]
        [:p (:due task)]]
      [:div.Task-right
        [:p (:details task)]
        [:div.TasklabelHolder
          (for [label labelDisplay]
            [:button.TaskLabel {:key (:id label) :style {:background (:color label)}} (:name label)])]]

]))