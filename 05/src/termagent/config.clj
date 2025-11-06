(ns termagent.config)

(def ^:private default-base-url "https://api.openai.com")
(def ^:private default-model "gpt-4o-mini")

(defn base-url
  "Return the configured OpenAI base URL or the default value."
  []
  (or (System/getenv "OPENAI_BASE_URL")
      default-base-url))

(defn model
  "Return the configured model name or the default value."
  []
  (or (System/getenv "OPENAI_MODEL")
      default-model))

(defn api-key
  "Return the configured OpenAI API key."
  []
  (System/getenv "OPENAI_API_KEY"))
