(ns status-im.ui.screens.advanced-settings.views
  (:require [re-frame.core :as re-frame]
            [status-im.i18n.i18n :as i18n]
            [quo.core :as quo]
            [status-im.utils.config :as config]
            [status-im.utils.platform :as platform]
            [status-im.ui.components.list.views :as list])
  (:require-macros [status-im.utils.views :as views]))

(defn- normal-mode-settings-data [{:keys [network-name
                                          current-log-level
                                          waku-bloom-filter-mode
                                          wakuv2-flag
                                          communities-enabled?
                                          current-fleet
                                          webview-debug]}]
  (keep
   identity
   [{:size                 :small
     :title                (i18n/label :t/network)
     :accessibility-label  :network-button
     :container-margin-top 8
     :on-press
     #(re-frame/dispatch [:navigate-to :network-settings])
     :accessory            :text
     :accessory-text       network-name
     :chevron              true}
    {:size                 :small
     :title                (i18n/label :t/network-info)
     :accessibility-label  :network-button
     :container-margin-top 8
     :on-press
     #(re-frame/dispatch [:navigate-to :network-info])
     :chevron              true}
    {:size                :small
     :title               (i18n/label :t/log-level)
     :accessibility-label :log-level-settings-button
     :on-press
     #(re-frame/dispatch [:navigate-to :log-level-settings])
     :accessory           :text
     :accessory-text      current-log-level
     :chevron             true}
    {:size                :small
     :title               (i18n/label :t/fleet)
     :accessibility-label :fleet-settings-button
     :on-press
     #(re-frame/dispatch [:navigate-to :fleet-settings])
     :accessory           :text
     :accessory-text      current-fleet
     :chevron             true}
    {:size                :small
     :title               (i18n/label :t/bootnodes)
     :accessibility-label :bootnodes-settings-button
     :on-press
     #(re-frame/dispatch [:navigate-to :bootnodes-settings])
     :chevron             true}
    {:size                 :small
     :title                (i18n/label :t/rpc-usage-info)
     :accessibility-label  :rpc-usage-info
     :container-margin-top 8
     :on-press
     #(re-frame/dispatch [:navigate-to :rpc-usage-info])
     :chevron              true}
    (when platform/ios?
      {:size                :small
       :title               (i18n/label :t/notification-settings)
       :accessibility-label :advanced-notification-settings
       :on-press
       #(re-frame/dispatch [:navigate-to :notifications-advanced-settings])
       :chevron             true})
    ;; If it's enabled in the config, we don't show the option
    (when (not config/communities-enabled?)
      {:size                   :small
       :title                   (i18n/label :t/communities-enabled)
       :accessibility-label     :communities-enabled
       :container-margin-bottom 8
       :on-press
       #(re-frame/dispatch
         [:multiaccounts.ui/switch-communities-enabled (not communities-enabled?)])
       :accessory               :switch
       :active                  communities-enabled?})
    {:size                   :small
     :title                   "Webview debug"
     :accessibility-label     :webview-debug-switch
     :container-margin-bottom 8
     :on-press
     #(re-frame/dispatch
       [:multiaccounts.ui/switch-webview-debug (not webview-debug)])
     :accessory               :switch
     :active                  webview-debug}
    {:size                    :small
     :title                   (i18n/label :t/waku-bloom-filter-mode)
     :accessibility-label     :waku-bloom-filter-mode-settings-switch
     :container-margin-bottom 8
     :on-press
     #(re-frame/dispatch
       [:multiaccounts.ui/waku-bloom-filter-mode-switched (not waku-bloom-filter-mode)])
     :accessory               :switch
     :active                  waku-bloom-filter-mode}
    {:size                    :small
     :title                   (i18n/label :t/wakuv2-flag)
     :accessibility-label     :waku-v2-settings-switch
     :container-margin-bottom 8
     :on-press
     #(re-frame/dispatch
       [:multiaccounts.ui/wakuv2-switched (not wakuv2-flag)])
     :accessory               :switch
     :active                   wakuv2-flag}]))

(defn- flat-list-data [options]
  (normal-mode-settings-data options))

(defn- render-item [props]
  (if (= (:type props) :section-header)
    [quo/list-header (:title props)]
    [quo/list-item props]))

(views/defview advanced-settings []
  (views/letsubs [{:keys [webview-debug]} [:multiaccount]
                  network-name             [:network-name]
                  waku-bloom-filter-mode   [:waku/bloom-filter-mode]
                  wakuv2-flag              [:waku/v2-flag]
                  communities-enabled?     [:communities/enabled?]
                  current-log-level        [:log-level/current-log-level]
                  current-fleet            [:fleets/current-fleet]]
    [list/flat-list
     {:data      (flat-list-data
                  {:network-name           network-name
                   :current-log-level      current-log-level
                   :communities-enabled?   communities-enabled?
                   :current-fleet          current-fleet
                   :dev-mode?              false
                   :waku-bloom-filter-mode waku-bloom-filter-mode
                   :wakuv2-flag            wakuv2-flag
                   :webview-debug          webview-debug})
      :key-fn    (fn [_ i] (str i))
      :render-fn render-item}]))
