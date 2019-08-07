(ns work-tasks.views.calendar
  (:require [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.scripts.calendarHelpers :as calendarHelpers]
            ["moment" :as moment]))

(defn render [active tasks]
  (let [calendarDates (calendarHelpers/generate-calendar-display (moment))]
    (fn [active tasks]
      (print calendarDates)
      [:div..Page.Calendar {:class active}
        [:div.Calendar.header
          [:p {:on-click #(handle-state-change {:type "update-active-view" :value "home"})}"Go Back"]]
        [:div.Calendar.content
          [:h4 "Your Week"]
          [:div.Calendar.calendar
            (for [date calendarDates]
              [:p {:key (.format date "DD")} (.format date "DD")])]
          [:div.Calendar.todaysTask
            [:h3 "Today's Tasks"]]
          [:h2 "I'm the calendar"]]])))