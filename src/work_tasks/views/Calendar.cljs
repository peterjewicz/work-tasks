(ns work-tasks.views.calendar
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.scripts.calendarHelpers :as calendarHelpers]))

(defn render [active tasks]
  (print (calendarHelpers/get-tasks-due-today tasks))
  [:div..Page.Calendar {:class active}
    [:div.Calendar.header
      [:p {:on-click #(handle-state-change {:type "update-active-view" :value "home"})}"Go Back"]]
    [:div.Calendar.content
      [:div.Calendar.calendar
        [:p "this is where our actual calendar will go"]]
      [:div.Calendar.todaysTask
        [:h3 "Today's Tasks"]]
      [:h2 "I'm the calendar"]]])