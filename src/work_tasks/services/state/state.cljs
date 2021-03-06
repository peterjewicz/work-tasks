(ns work-tasks.services.state.state
    (:require [reagent.core :as reagent :refer [atom]]))

; Some state items get overriden on update (like active page)
; But I like to define them all here like this as sort of a documentation of sorts, can see valid values here
(defonce app-state (atom {:text "Hello world!"
                          :active-task nil ;holds a ref to the active task for editing purposes
                          :previous-page nil
                          :active-page {:home "active"
                                        :task false
                                        :calendar false
                                        :settings false}
                          :notification {:message "Task Saved!" :background "green" :display 1}}))

; TODO move these two scrolling functions into a state helper file
; Don't want them cluttering up this namespace
(defn update-scroll-position [val scroll]
  "Updates the store with our current scroll position to re-position back on home view"
  (if scroll
      (.scrollTo js/window 0 (:scrollOffset @app-state)))
  (swap! app-state conj {:scrollOffset val}))

(defn handle-scroll-func [payload]
  "adds class to body preventing weird scroll on fixed position over scrolling main window"
  (if (= payload "")
    (do
      (.remove (.-classList (.-body js/document)) "hide-scroll")
      (update-scroll-position 0 true)) ; this should be instant
    (do
      (update-scroll-position (.-pageYOffset js/window) false)
      (js/setTimeout #(.add (.-classList (.-body js/document)) "hide-scroll") 100))))

(defn update-active-view [app-state payload]
  (let [actualPrevious (:previous-page @app-state)] ; prevents the swap from mucking things up
    (swap! app-state conj {:previous-page (name (first (first (:active-page @app-state))))})
    (if (= payload "previous") ; Tasks page wants to always go back to the last page it was on - account for that
      (swap! app-state conj {:active-page {(keyword actualPrevious) "active"}})
      (swap! app-state conj {:active-page {(keyword payload) "active"}}))
  (handle-scroll-func payload)))
  ;TODO need to make sure that scroll func properly handles going from task -> cal page -> home
