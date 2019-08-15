(ns work-tasks.components.LabelSelector
  (:require [reagent.core :as reagent :refer [atom]]))

(defn toggle-label-dropdown [showDropdown value]
  "Show/hide label selector dropdown"
  (reset! showDropdown value))

(defn generate-value [selectedLabels labels]
  "generats our default value for the input dispaly"
  (if (> (count selectedLabels) 0) ; if we have selectedLabels
    (reduce (fn [acc label]
              (str acc
                   (:name (first (filter (fn [globalLabel]
                      (= (:id globalLabel) label)) labels))) " ")) "" selectedLabels)
    "No Label"))

(defn is-label-added? [label labels]
  (if (> (count (filter (fn [selectedLabels]
        (= (:id label) selectedLabels)) labels)) 0)
    true
    false))

(defn remove-label [id onSelectLabel]
  "Removes a label from the task"
  (onSelectLabel id true))

(defn generate-label-buttons [selectedLabels labels onSelectLabel]
  "Generates selected label buttons to populate the input"
  (for [label selectedLabels]
    (let [labelDetails (first (filter (fn [globalLabel]
          (= (:id globalLabel) label)) labels))]
      [:button.removeLabelButton
        {:key (:id labelDetails)
         :style {:background (:color labelDetails)}
         :on-click #(remove-label (:id labelDetails) onSelectLabel)}
        (str (:name labelDetails) "... X")])))

;labels - all our current labels
;selectedLabels - currently selected labels to populate UI
(defn LabelSelector [labels selectedLabels onSelectLabel]
  (let [showDropdown (atom false)]
    (fn [labels selectedLabels]
      [:div
        [:div.LabelSelectorInputHolder
          [:input {:type "text"
                   :readOnly true
                   :placeholder "No Labels"}]
          [:div.LabelSelectorInputHolderInner {:on-click #(toggle-label-dropdown showDropdown true)}
            (generate-label-buttons selectedLabels labels onSelectLabel)]]
        [:div.labelDropdownContainer {:class @showDropdown}
          [:p {:on-click #(toggle-label-dropdown showDropdown false)}"X"]
          (doall (for [label labels]
            (if (not (is-label-added? label selectedLabels))
            [:div.labelDropdown.label {:key (:id label)}
              [:button {:style {:background (:color label)}
                        :on-click #(onSelectLabel (:id label))
                       } (:name label)]])))]])))