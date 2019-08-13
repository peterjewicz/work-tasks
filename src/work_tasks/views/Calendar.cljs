(ns work-tasks.views.calendar
  (:require [reagent.core :as reagent :refer [atom]]
            [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.scripts.calendarHelpers :as calendarHelpers]
            [work-tasks.scripts.taskHelpers :as taskHelpers]
            [work-tasks.components.task :refer [Task]]
            [work-tasks.components.Calendar :as Calendar]
            ["moment" :as moment]))

(defn render [active tasks]
  (let [calendarDates (calendarHelpers/generate-calendar-display (moment))
        todaysTasks (taskHelpers/get-tasks-due-on-date (.format (moment) "MM/DD/YYYY") tasks)
        currentDate (atom (.format (moment) "MM/DD/YYYY"))]
    (fn [active tasks]
      [:div..Page.Calendar {:class active}
        [:div.Calendar.header
          [:p {:on-click #(handle-state-change {:type "update-active-view" :value "home"})}"Go Back"]]
        [:div.Calendar.content
          [Calendar/render tasks currentDate]
          [:div.Calendar.todaysTask
            [:h3 (str "Tasks For " @currentDate)]
              (if (> (count (taskHelpers/get-tasks-due-on-date @currentDate tasks)) 0)
                (for [task (taskHelpers/get-tasks-due-on-date @currentDate tasks)]
                  (Task task))
                [:p "No Tasks For Selected Date"])]]])))