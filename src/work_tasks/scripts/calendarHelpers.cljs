(ns work-tasks.scripts.calendarHelpers
  (:require ["moment" :as moment]))

; This file is a series of functions used for calendar dispaly and processing

(defn get-tasks-due-today [tasks]
  "retuns all of the tasks that are due today"
    (let [todaysDate (.format (moment) "MM/DD/YYYY")]
      (filter (fn [task] (= (:due task) todaysDate)) tasks)))

(defn get-tasks-due-on-date [tasks date]
  "takes a date formatted 'MM/DD/YYYY' and returns tasks due on that date")

(defn generate-calendar-display [startDate]
  "generates display - weeks worth of days centered on 'startDate' N +- 3")