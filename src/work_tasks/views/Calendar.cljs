(ns work-tasks.views.calendar
  (:require [reagent.core :as reagent :refer [atom]]
            [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.scripts.calendarHelpers :as calendarHelpers]
            [work-tasks.scripts.taskHelpers :as taskHelpers]
            [work-tasks.components.task :refer [Task]]
            [work-tasks.components.Calendar :as Calendar]
            ["moment" :as moment]))

(defn get-class-for-today [date1 date2]
  "adds a class to the element that is currently the active date for the user"
  (if (= date1 date2)
    "active-date"
    nil))

(defn update-active-date [newDate currentDate]
  "sets the date passed in to active for display stuff TODO call new tasks for date"
  (reset! currentDate newDate))

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
              (for [task (taskHelpers/get-tasks-due-on-date @currentDate tasks)]
                (Task task))]]])))