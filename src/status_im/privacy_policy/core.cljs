(ns status-im.privacy-policy.core
  (:require [re-frame.core :as re-frame]
            [status-im.ui.components.react :as react]
            [status-im.utils.fx :as fx]))

(def privacy-policy-link "https://status.im/privacy-policy/")

(defn open-privacy-policy-link! []
  (.openURL ^js react/linking privacy-policy-link))

(re-frame/reg-fx
 :privacy-policy/open-privacy-policy-link
 (fn []
   (open-privacy-policy-link!)))

(fx/defn open-privacy-policy-link
  {:events [:privacy-policy/privacy-policy-button-pressed]}
  [_]
  {:privacy-policy/open-privacy-policy-link nil})
