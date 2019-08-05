(ns work-tasks.scripts.notifications
  (:require [reagent.core :as reagent :refer [atom]]
            [work-tasks.services.state.dispatcher :refer [handle-state-change]]))
; This handles notifications to the browser

;TODO
;Need to provide a method to change the background color - error and success ones?
;Might just need success for this for the time being though.

(defonce styles {
  ;Our styles map still TODO
  :background "null"
  :color "null"
  :height "null"
  :font-size "null"
})

(defn update-notification [toRemove toAdd]
  "updates the notifiction state - Takes the class to remove and add so can be re-used for both"
  (let [notification (.getElementById js/document "Notification")]
    (.remove (.-classList notification) toRemove)
    (.add (.-classList notification) toAdd)))

(defn handle-close-notification []
  (update-notification "active" "hidden")
  (handle-state-change {:type "update-notification-state" :value {:message "" :background "" :display false}}))

(defn setup-timeout []
  "sets up the time out to close the notification"
  (js/setTimeout #(handle-close-notification) 1250))

(defn show-notification []
  "handles the showing of the notification"
  (let [notification (.getElementById js/document "Notification")]
    (update-notification "hidden" "active")))

(defn handle-show-notification []
  (show-notification)
  (setup-timeout))

(defn Notification [message-details]
  "Renders the notifications, takes a message to display - our create function
   Should probably remove the reagent stuff so that we can create a bunch of these"

  (let [toUpdate (atom (:display message-details))]
    (fn [message-details]
      (print (:display message-details))
      (if (and  (not (= @toUpdate (:display message-details))) (not (= false (:display message-details)))) ; if our @toUpdate is different we know that we can show a notification
        (handle-show-notification))
      [:div#Notification.Notification  {:class "hidden" :style {:background-color (:background message-details)}}
        [:div.NotificationInner
          [:p.NotificationMessage (:message message-details)]]])))


