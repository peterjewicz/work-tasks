(ns work-tasks.scripts.notifications
  (:require [reagent.core :as reagent :refer [atom]]))
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
  (let [notification (.getElementById js/document "Notification")])
    (.remove (.-classList "notification") toRemove)
    (.add (.-classList "notification") toAdd))

(defn setup-timeout []
  "sets up the time out to close the notification"
  (js/setTimeout (fn [] (update-notification "active" "hiden") 2000)))

(defn show-notification []
  "handles the showing of the notification"
  (let [notification (.getElementById js/document "Notification")]
    (update-notification "hidden" "active")))

(defn this-is-a-test []
  (js/alert "test"))

(defn Notification [message-details]
  "Renders the notifications, takes a message to display - our create function
   Should probably remove the reagent stuff so that we can create a bunch of these"

  (let [toUpdate (atom (:display message-details))]
    (fn [message-details]
  ; (setup-timeout)
      (print message-details)
      (if (not (= @toUpdate (:display message-details)))
        (this-is-a-test))
      [:div#Notification.Notification  {:class "hidden" :style {:background-color (:background message-details)}}
        [:div.NotificationInner
          [:p.NotificationMessage (:message message-details)]]])))


