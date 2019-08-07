(ns work-tasks.scripts.calendarHelpers
  (:require ["moment" :as moment]))

; This file is a series of functions used for calendar dispaly and processing

(defn get-n-days-after [date n]
  "returns a date object corresponding to n days after the given date
   Note: `date` is a moment.js object"
  (.add (moment date) n "days")); we wrap it in another moment here or else we mutate the the moment on subtract/add
  ;might be more efficient just to let this happen, but this feels more correct functionally and that's a bit premature

(defn get-n-days-before [date n]
  "returns a date objects corresponding to n days before the given date
   Note: `date` is a moment.js object"
  (.subtract (moment date) n "days"))

(defn get-tasks-due-today [tasks]
  "retuns all of the tasks that are due today"
    (let [todaysDate (.format (moment) "MM/DD/YYYY")]
      (filter (fn [task] (= (:due task) todaysDate)) tasks)))

(defn get-tasks-due-on-date [tasks date]
  "takes a date formatted 'MM/DD/YYYY' and returns tasks due on that date")

(defn generate-past-display [date n]
  "called from generate-calendar-display - returns a vector of n days in the past"
  (loop [i n
         dateVector []]
    (if (= 0 i)
      dateVector
      (recur (- i 1) (conj dateVector (get-n-days-before date i))))))

(defn generate-future-display [date n]
  "called from generate-calendar-display - returns a vector of n days in the future"
  (loop [i 1
         dateVector []]
    (if (> i n)
      dateVector
      (recur (inc i) (conj dateVector (get-n-days-after date i))))))

(defn generate-calendar-display [startDate]
  "generates display - weeks worth of days centered on 'startDate' N +- 3"
  (concat
    (generate-past-display startDate 3)
    [startDate]
    (generate-future-display startDate 3)))