(ns work-tasks.components.Label)




(defn Label [details]
  [:div.Label {:key (:id details)}
    [:div.Label-left
      [:input {:type "test" :defaultValue (:name details)}]
      [:div.Label-color {:style {:background (:color details)}}]]
    [:div.Label-right
      [:button.primary "Update"]]])