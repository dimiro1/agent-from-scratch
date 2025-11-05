(ns termagent.core
  (:require [termagent.openai :as openai]
            [termagent.markdown :as md]
            [termagent.ansi :as ansi]))

(defn read-user-input []
  (print (ansi/render [:bold :cyan "User: "]))
  (flush)
  (read-line))

(defn -main []
  (loop [history []]
    (let [input (read-user-input)]
      (case input
        "exit" (println "Bye")
        nil? (println "Bye")
        (do
          (let [baseurl "https://api.openai.com/v1"
                api-key (System/getenv "OPENAI_API_KEY")
                model "gpt-4o-mini"
                history (conj history {:role "user" :content input})
                response (openai/generate baseurl api-key model history)
                ai-reply (openai/get-reply response)]
            (println (ansi/render [:bold :green "Assistant:"]))
            (println (md/render ai-reply))
            (recur (conj history {:role "assistant" :content ai-reply}))))))))
