(ns work-tasks.views.home
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.scripts.taskHelpers :refer [filter-completed-tasks]]
            [work-tasks.components.task :refer [Task]]))

(defn generate-task-display [tasks]
  "Generates our html for displaying home page tasks"
  [:div.HomeTaskWrapper
    [:h1 "Your Tasks"]
    (for [task tasks]
      (Task task))])

(defn generate-no-tasks-display []
  "no tasks display"
  [:h2.noTasksText "You Don't Have Any Tasks. Try Adding Some!"])

(defn generate-home-ad-padding [tasks]
  (if (> (count tasks 4))
    "add-padding"))

(defn render [active tasks]
  [:div.Page.Home {:class (str active " " (generate-home-ad-padding tasks))}
    [:div.Home.header
      [:i.fas.fa-cog {:on-click #(handle-state-change {:type "update-active-view" :value "settings"})}]
      [:i.fas.fa-calendar-alt {:on-click #(handle-state-change {:type "update-active-view" :value "calendar"})}]]
    (let [notCompletedTasks (filter-completed-tasks tasks)]
      (if (> (count notCompletedTasks) 0)
        (generate-task-display notCompletedTasks)
        (generate-no-tasks-display)))
    [:div.addTaskButton {:on-click #(handle-state-change {:type "update-active-view" :value "task"})}
      [:p "+"]]])