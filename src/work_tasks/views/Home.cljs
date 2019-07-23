(ns work-tasks.views.home
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]))

(defn generate-task-display [tasks]
  "Generates our html for displaying home page tasks"
  (for [task tasks]
    [:div.HomeTask
      [:h2 (:title task)]
      [:p (:details task)]]))

(defn generate-no-tasks-display []
  "no tasks display"
  [:h2 "You Don't Have Any Tasks. Try Adding Some!"])

(defn render [active tasks]
  [:div.Page {:class active}
    (if tasks
      (generate-task-display tasks)
      (generate-no-tasks-display))
    [:div.addTaskButton {:on-click #(handle-state-change {:type "update-active-view" :value "task"})}
      [:p "+"]]])