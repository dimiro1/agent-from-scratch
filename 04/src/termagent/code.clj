(ns termagent.code
  (:require [clojure.string :as str]
            [termagent.ansi :as ansi]))

(defn highlight-clojure [code]
  (-> code
      ;; Strings (must come before keywords to avoid conflicts)
      (str/replace #"\".*?\""
                   (fn [match]
                     (ansi/render [:green match])))
      ;; Comments
      (str/replace #"(?m);.*$"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Keywords (Clojure :keyword syntax)
      (str/replace #":[a-zA-Z][a-zA-Z0-9\-]*"
                   (fn [match]
                     (ansi/render [:magenta match])))
      ;; Numbers
      (str/replace #"\b\d+\.?\d*\b"
                   (fn [match]
                     (ansi/render [:yellow match])))
      ;; Common functions
      (str/replace #"\b(println|print|str|map|reduce|filter|conj|assoc|dissoc|get|first|rest|count|into|vec|list|seq|apply|partial|comp)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))
      ;; Language keywords
      (str/replace #"\b(def|defn|defn-|defmacro|let|if|when|cond|fn|loop|recur|do|for|doseq|require|ns|use)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))))

(defn highlight-python [code]
  (-> code
      ;; Strings (single and double quotes, must come first)
      (str/replace #"\"\"\"[\s\S]*?\"\"\"|'''[\s\S]*?'''|\".*?\"|'.*?'"
                   (fn [match]
                     (ansi/render [:green match])))
      ;; Comments
      (str/replace #"(?m)#.*$"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Numbers
      (str/replace #"\b\d+\.?\d*\b"
                   (fn [match]
                     (ansi/render [:yellow match])))
      ;; Language keywords
      (str/replace #"\b(def|class|if|else|elif|for|while|return|import|from|as|try|except|finally|with|lambda|pass|break|continue|yield|async|await)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))))

(defn highlight-javascript [code]
  (-> code
      ;; Strings (single, double quotes, and template literals, must come first)
      (str/replace #"`.*?`|\".*?\"|'.*?'"
                   (fn [match]
                     (ansi/render [:green match])))
      ;; Comments
      (str/replace #"(?m)//.*$"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Numbers
      (str/replace #"\b\d+\.?\d*\b"
                   (fn [match]
                     (ansi/render [:yellow match])))
      ;; Language keywords
      (str/replace #"\b(function|const|let|var|if|else|for|while|return|class|new|this|async|await|try|catch|finally|throw|import|export|from|default)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))))

(defn highlight-java [code]
  (-> code
      ;; Strings (must come first)
      (str/replace #"\".*?\""
                   (fn [match]
                     (ansi/render [:green match])))
      ;; Comments
      (str/replace #"(?m)//.*$"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Numbers
      (str/replace #"\b\d+\.?\d*[fFdDlL]?\b"
                   (fn [match]
                     (ansi/render [:yellow match])))
      ;; Language keywords
      (str/replace #"\b(public|private|protected|static|final|class|interface|extends|implements|void|int|long|float|double|boolean|char|String|if|else|for|while|return|new|this|super|try|catch|finally|throw|throws|import|package)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))))

(defn highlight-elixir [code]
  (-> code
      ;; Strings (must come first)
      (str/replace #"\".*?\""
                   (fn [match]
                     (ansi/render [:green match])))
      ;; Comments
      (str/replace #"(?m)#.*$"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Atoms
      (str/replace #":[a-zA-Z_][a-zA-Z0-9_]*"
                   (fn [match]
                     (ansi/render [:magenta match])))
      ;; Numbers
      (str/replace #"\b\d+\.?\d*\b"
                   (fn [match]
                     (ansi/render [:yellow match])))
      ;; Language keywords
      (str/replace #"\b(def|defp|defmodule|defmacro|do|end|if|unless|case|cond|when|fn|for|with|else|true|false|nil)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))))

(defn highlight-lua [code]
  (-> code
      ;; Strings (single and double quotes, must come first)
      (str/replace #"\".*?\"|'.*?'"
                   (fn [match]
                     (ansi/render [:green match])))
      ;; Comments
      (str/replace #"(?m)--.*$"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Numbers
      (str/replace #"\b\d+\.?\d*\b"
                   (fn [match]
                     (ansi/render [:yellow match])))
      ;; Language keywords
      (str/replace #"\b(function|local|if|then|else|elseif|end|for|while|do|repeat|until|return|break|and|or|not|true|false|nil)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))))

(defn highlight-ruby [code]
  (-> code
      ;; Strings (single and double quotes, must come first)
      (str/replace #"\".*?\"|'.*?'"
                   (fn [match]
                     (ansi/render [:green match])))
      ;; Comments
      (str/replace #"(?m)#.*$"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Symbols
      (str/replace #":[a-zA-Z_][a-zA-Z0-9_]*"
                   (fn [match]
                     (ansi/render [:magenta match])))
      ;; Numbers
      (str/replace #"\b\d+\.?\d*\b"
                   (fn [match]
                     (ansi/render [:yellow match])))
      ;; Language keywords
      (str/replace #"\b(def|class|module|if|else|elsif|unless|case|when|while|until|for|do|end|begin|rescue|ensure|return|yield|break|next|redo|retry|super|self|true|false|nil|require|include|extend|attr_reader|attr_writer|attr_accessor)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))))

(defn highlight
  "Applies syntax highlighting to code based on the language"
  [code language]
  (case (str/lower-case (str/trim language))
    "clojure" (highlight-clojure code)
    "clj" (highlight-clojure code)
    "python" (highlight-python code)
    "py" (highlight-python code)
    "javascript" (highlight-javascript code)
    "js" (highlight-javascript code)
    "java" (highlight-java code)
    "elixir" (highlight-elixir code)
    "ex" (highlight-elixir code)
    "lua" (highlight-lua code)
    "ruby" (highlight-ruby code)
    "rb" (highlight-ruby code)
    (ansi/render [:dim code])))

(defn showcase []
  (println "\n=== Clojure ===")
  (println (highlight "(defn greet [name]
  ; Say hello with a number
  (println \"Hello,\" name :world 42))

(greet \"Alice\")" "clojure"))

  (println "\n=== Python ===")
  (println (highlight "def greet(name):
    # Print greeting
    count = 10
    return \"Hello, \" + name + str(count)

greet('Alice')" "python"))

  (println "\n=== JavaScript ===")
  (println (highlight "function greet(name) {
    // Print greeting
    const count = 42;
    return `Hello, ${name} - ${count}`;
}

greet('Alice');" "javascript"))

  (println "\n=== Java ===")
  (println (highlight "public class Greeter {
    // Say hello
    public static String greet(String name) {
        int count = 100;
        return \"Hello, \" + name + count;
    }
}" "java"))

  (println "\n=== Elixir ===")
  (println (highlight "defmodule Greeter do
  # Say hello
  def greet(name) do
    count = 42
    \"Hello, #{name} - #{count}\" <> :world
  end
end" "elixir"))

  (println "\n=== Lua ===")
  (println (highlight "function greet(name)
    -- Say hello
    local count = 3.14
    return \"Hello, \" .. name .. count
end

greet('Alice')" "lua"))

  (println "\n=== Ruby ===")
  (println (highlight "class Greeter
  # Say hello
  def greet(name)
    count = 42
    \"Hello, #{name} - #{count}\" + :world.to_s
  end
end

Greeter.new.greet('Alice')" "ruby")))
