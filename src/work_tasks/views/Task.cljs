(ns work-tasks.views.task
  (:require [reagent.core :as reagent :refer [atom]]
            [work-tasks.scripts.api :as api]
            [work-tasks.components.datepicker :as datepicker]
            [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            ["moment" :as moment]))

; TODO update the dispaly on a succesful new save but not on edit!
; Date won't properly populat on edits

(defn leave-task-page [taskDetails taskKeys]
  "We touch our state here so it doesn't carry over to new" ;; TODO REMOVE THIS IS FOR TESTING ONLY
  (reset! taskDetails {:title "" :details "" :id false})
  (reset! taskKeys [(inc (first @taskKeys)) (inc (second @taskKeys)) (inc (nth @taskKeys 2))])
  (handle-state-change {:type "update-active-view" :value "previous"}))

(defn merge-dates-on-save [taskDetails startTime]
  "Merges our date atom onto the main details before saving"
  (conj taskDetails {:due startTime}))


; NOTE
; If during render `:id` is set we know this is an existing task in edit mode
(defn render [active activeTask]
  (let [taskDetails (atom {:title "" :details "" :id false })
        taskKeys (atom [1, 2, 3])
        startTime (atom (.format (moment (js/Date. (.setHours (js/Date.) 0 0 0 0))) "MM/DD/YYYY"))] ; we use this to force re-render on the input
    (fn [active activeTask]
      (if activeTask
        (do
          (swap! taskDetails conj {:title (:title activeTask) :details (:details activeTask) :id (:id activeTask)}); TODO there's probably a merge function that just does this
          (reset! startTime (:due activeTask)) ; we need to set the date we pass to the datepicker too
          (reset! taskKeys [(inc (first @taskKeys)) (inc (second @taskKeys)) (inc (nth @taskKeys 2))]) ; TODO make this better
          (handle-state-change {:type "update-active-task" :value nil})
        ))
      [:div.Task.Page {:class active}
        [:div.Task.header
          [:p {:on-click #(leave-task-page taskDetails taskKeys)} "Go Back"]]
      [:div.Task.body
        [:div.inputWrapper
          [:h4 "Task Name"]
          [:input {:type "text" :placeholder "Give your task a name." :key (first @taskKeys) :defaultValue (:title @taskDetails) :name "title" :on-change #(swap! taskDetails conj {:title (-> % .-target .-value)})}]]
        [:div.inputWrapper
          [:h4 "Task Details"]
          [:textarea {:placeholder "Add details to the task." :key (second @taskKeys) :defaultValue (:details @taskDetails) :name "details" :on-change #(swap! taskDetails conj {:details (-> % .-target .-value)})}]]
        [:div.inputWrapper
          [:h4 "Due Date"]
          [datepicker/datepicker startTime]]
        [:button.primary {:on-click #(api/save-task (merge-dates-on-save @taskDetails @startTime))} "Save"]
        (if (:id @taskDetails)
          [:div.completeButtonWrapper
            [:button.success {:on-click #(api/save-task  (conj (merge-dates-on-save @taskDetails @startTime) {:completed? true :completedOn (js/Date.)}))} "Complete"]
            [:button.warning {:on-click #(api/delete-task (:id @taskDetails))} "Delete"]])]])))