(ns work-tasks.components.LabelSelector
  (:require [reagent.core :as reagent :refer [atom]]))

(defn toggle-label-dropdown [showDropdown]
  "Show/hide label selector dropdown"
  (reset! showDropdown (not @showDropdown)))

(defn generate-value [selectedLabels labels]
  "generats our default value for the input dispaly"
  (if (> (count selectedLabels) 0) ; if we have selectedLabels
    (reduce (fn [acc label]
              (str acc
                   (:name (first (filter (fn [globalLabel]
                      (= (:id globalLabel) label)) labels))) " ")) "" selectedLabels)
    "No Label"))

;labels - all our current labels
;selectedLabels - currently selected labels to populate UI
(defn LabelSelector [labels selectedLabels onSelectLabel]
  (let [showDropdown (atom false)]
    (fn [labels selectedLabels]
      [:div
        [:input {:type "text"
                 :on-click #(toggle-label-dropdown showDropdown)
                 :readOnly true
                 :value (generate-value selectedLabels labels)
                 :placeholder "No Labels"}]
        [:div.labelDropdownContainer {:class @showDropdown}
          (doall (for [label labels]
            [:div.labelDropdown.label {:key (:id label)}
              [:button {:style {:background (:color label)}
                        :on-click #(onSelectLabel (:id label))
                       } (:name label)]]))]])))