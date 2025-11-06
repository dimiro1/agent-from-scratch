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

(defn highlight-typescript [code]
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
      ;; Language keywords (TypeScript specific)
      (str/replace #"\b(function|const|let|var|if|else|for|while|return|class|new|this|async|await|try|catch|finally|throw|import|export|from|default|interface|type|enum|namespace|declare|public|private|protected|readonly|static|extends|implements)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))))

(defn highlight-jsx [code]
  (-> code
      (highlight-javascript)
      ;; HTML/JSX tags
      (str/replace #"</?[A-Z][a-zA-Z0-9]*|</?[a-z][a-z0-9]*"
                   (fn [match]
                     (ansi/render [:magenta match])))
      ;; CSS property names (in style objects or css`` templates)
      (str/replace #"\b[a-zA-Z-]+(?=:)"
                   (fn [match]
                     (ansi/render [:cyan match])))))

(defn highlight-tsx [code]
  (-> code
      (highlight-typescript)
      ;; HTML/JSX tags
      (str/replace #"</?[A-Z][a-zA-Z0-9]*|</?[a-z][a-z0-9]*"
                   (fn [match]
                     (ansi/render [:magenta match])))
      ;; CSS property names (in style objects or css`` templates)
      (str/replace #"\b[a-zA-Z-]+(?=:)"
                   (fn [match]
                     (ansi/render [:cyan match])))))

(defn highlight-html [code]
  (-> code
      ;; HTML comments
      (str/replace #"<!--.*?-->"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Attribute values (strings)
      (str/replace #"\".*?\"|'.*?'"
                   (fn [match]
                     (ansi/render [:green match])))
      ;; HTML tags
      (str/replace #"</?[a-zA-Z][a-zA-Z0-9]*"
                   (fn [match]
                     (ansi/render [:magenta match])))
      ;; Attribute names
      (str/replace #"\b[a-zA-Z-]+="
                   (fn [match]
                     (ansi/render [:cyan match])))))

(defn highlight-css [code]
  (-> code
      ;; CSS comments
      (str/replace #"(?s)/\*.*?\*/"
                   (fn [match]
                     (ansi/render [:dim match])))
      ;; Strings
      (str/replace #"\".*?\"|'.*?'"
                   (fn [match]
                     (ansi/render [:green match])))
      ;; Selectors (class, id)
      (str/replace #"[.#][a-zA-Z][a-zA-Z0-9_-]*"
                   (fn [match]
                     (ansi/render [:magenta match])))
      ;; Property names
      (str/replace #"(?m)^\s*[a-zA-Z-]+(?=:)"
                   (fn [match]
                     (ansi/render [:cyan match])))
      ;; Numbers and units
      (str/replace #"\b\d+\.?\d*(px|em|rem|%|vh|vw|ms|s)?\b"
                   (fn [match]
                     (ansi/render [:yellow match])))))

(defn highlight-go [code]
  (-> code
      ;; Strings (double quotes and raw strings with backticks, must come first)
      (str/replace #"`[^`]*`|\".*?\""
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
      (str/replace #"\b(func|package|import|var|const|type|struct|interface|if|else|for|range|return|defer|go|chan|select|case|default|switch|break|continue|fallthrough|map|make|len|cap|new|append|copy|delete|panic|recover|print|println)\b"
                   (fn [[match]]
                     (ansi/render [:blue match])))
      ;; Built-in types
      (str/replace #"\b(int|int8|int16|int32|int64|uint|uint8|uint16|uint32|uint64|string|bool|byte|rune|float32|float64|complex64|complex128|error)\b"
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
    "jsx" (highlight-jsx code)
    "typescript" (highlight-typescript code)
    "ts" (highlight-typescript code)
    "tsx" (highlight-tsx code)
    "html" (highlight-html code)
    "css" (highlight-css code)
    "go" (highlight-go code)
    "golang" (highlight-go code)
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

  (println "\n=== TypeScript ===")
  (println (highlight "interface Greeter {
  greet(name: string): string;
}

function greet(name: string): string {
  // Print greeting
  const count: number = 42;
  return `Hello, ${name} - ${count}`;
}

greet('Alice');" "typescript"))

  (println "\n=== JSX ===")
  (println (highlight "function MyComponent() {
  // Render component
  const name = 'World';
  return (
    <div>
      <h1>Hello {name}</h1>
    </div>
  );
}" "jsx"))

  (println "\n=== TSX ===")
  (println (highlight "interface Props {
  name: string;
}

function MyComponent({ name }: Props) {
  return (
    <div>
      <h1>Hello {name}</h1>
    </div>
  );
}" "tsx"))

  (println "\n=== HTML ===")
  (println (highlight "<!DOCTYPE html>
<html>
  <head>
    <title>Hello World</title>
  </head>
  <body>
    <!-- Main content -->
    <div class=\"container\" id=\"main\">
      <h1>Hello World</h1>
      <p>Welcome to the page</p>
    </div>
  </body>
</html>" "html"))

  (println "\n=== CSS ===")
  (println (highlight "/* Styles for container */
.container {
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
}

#main {
  background-color: #f0f0f0;
  border-radius: 8px;
  font-size: 16px;
}

h1 {
  color: #333;
  font-weight: 700;
}" "css"))

  (println "\n=== Go ===")
  (println (highlight "package main

import \"fmt\"

// Greet returns a greeting message
func Greet(name string) string {
    count := 42
    return fmt.Sprintf(`Hello, %s - %d`, name, count)
}

func main() {
    message := Greet(\"World\")
    fmt.Println(message)
}" "go"))

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
