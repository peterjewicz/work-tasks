(ns work-tasks.views.task
  (:require [reagent.core :as reagent :refer [atom]]
            [work-tasks.scripts.api :as api]
            [work-tasks.components.datepicker :as datepicker]
            [work-tasks.services.state.dispatcher :refer [handle-state-change]]
            [work-tasks.components.LabelSelector :refer [LabelSelector]]
            ["moment" :as moment]))

; TODO update the dispaly on a succesful new save but not on edit!
; Date won't properly populat on edits
; LABELS --------
; Check that it works on save
; Add UI colors to input - probably absolute positon divs with text
; give ability to remove a label - as above is probably enough with an `x`
; Walk through label flow to make sure it all behaves as intended

(defn leave-task-page [taskDetails taskKeys]
  "We touch our state here so it doesn't carry over to new" ;; TODO REMOVE THIS IS FOR TESTING ONLY
  (reset! taskDetails {:title "" :details "" :id false})
  (reset! taskKeys [(inc (first @taskKeys)) (inc (second @taskKeys)) (inc (nth @taskKeys 2))])
  (handle-state-change {:type "update-active-view" :value "previous"}))

(defn merge-dates-on-save [taskDetails startTime]
  "Merges our date atom onto the main details before saving"
  (conj taskDetails {:due startTime}))

(defn update-label-on-task
  "Updates an task with the label id
   multi arity pass id to add pass id and remove? to remove
   we store atoms by ID so that the name can change
   and we can reflect that since ID won't"
  ([taskDetails id]
    (swap! taskDetails update-in [:labels] merge id))
  ([taskDetails id remove?]
    (swap! taskDetails conj
      {:labels (filter (fn [label]
        (if (not= label id)
          true
          false)) (:labels @taskDetails))})))

(defn handle-delete-task [taskDetails]
  "Handle deleting a task and resetting the state for the task page"
  (api/delete-task (:id @taskDetails))
  (reset! taskDetails {:title "" :details "" :id false}))

; NOTE
; If during render `:id` is set we know this is an existing task in edit mode
(defn render [active activeTask labels]
  (let [taskDetails (atom {:title "" :details "" :id false :labels []})
        taskKeys (atom [1, 2, 3])
        startTime (atom (.format (moment (js/Date. (.setHours (js/Date.) 0 0 0 0))) "MM/DD/YYYY"))] ; we use this to force re-render on the input
    (fn [active activeTask labels]
      (if activeTask
        (do
          (swap! taskDetails conj {:title (:title activeTask) :details (:details activeTask) :id (:id activeTask) :labels (:labels activeTask)}); TODO there's probably a merge function that just does this
          (reset! startTime (:due activeTask)) ; we need to set the date we pass to the datepicker too
          (reset! taskKeys [(inc (first @taskKeys)) (inc (second @taskKeys)) (inc (nth @taskKeys 2))]) ; TODO make this better
          (handle-state-change {:type "update-active-task" :value nil})
        ))
      [:div.Task.Page {:class active}
        [:div.Task.header
          [:i.fas.fa-arrow-left {:on-click #(leave-task-page taskDetails taskKeys)}]]
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
        [:div.inputWrapper
          [:h4 "Labels"]
            [LabelSelector labels (:labels @taskDetails) (partial update-label-on-task taskDetails)]]
        [:button.primary {:on-click #(api/save-task (merge-dates-on-save @taskDetails @startTime))} "Save"]
        (if (:id @taskDetails)
          [:div.completeButtonWrapper
            [:button.success {:on-click #(api/save-task  (conj (merge-dates-on-save @taskDetails @startTime) {:completed? true :completedOn (js/Date.)}))} "Complete"]
            [:button.warning {:on-click #(handle-delete-task taskDetails)} "Delete"]])]])))