(ns work-tasks.views.home
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.scripts.taskHelpers :refer [filter-completed-tasks]]))

(defn dispatch-for-edit-view [task]
  "Handles calling the two functions to setup the edited view"
  ; (handle-state-change {:type "update-active-view" :value "task"})
  (handle-state-change {:type "update-active-task" :value task})
  (handle-state-change {:type "update-active-view" :value "task"}))

(defn generate-task-display [tasks]
  "Generates our html for displaying home page tasks"
  [:div.HomeTaskWrapper
    [:h1 "Your Tasks"]
    (for [task tasks]
      [:div.HomeTask {:key (:id task) :on-click #(dispatch-for-edit-view task)}
        [:div
          [:h2 (:title task)]
          [:p "Due: 11/2/2019 - 11:34PM"]]
        [:p (:details task)]])])

(defn generate-no-tasks-display []
  "no tasks display"
  [:h2 "You Don't Have Any Tasks. Try Adding Some!"])

(defn render [active tasks]
  [:div.Page.Home {:class active}
    [:div.Home.header
      [:p {:on-click #(handle-state-change {:type "update-active-view" :value "settings"})} "settings"]]
    (let [notCompletedTasks (filter-completed-tasks tasks)]
      (if (> (count notCompletedTasks) 0)
        (generate-task-display notCompletedTasks)
        (generate-no-tasks-display)))
    [:div.addTaskButton {:on-click #(handle-state-change {:type "update-active-view" :value "task"})}
      [:p "+"]]])