
let db = null;
async function createDatabase() {
  db = {}
}

function onModuleReady() {
  const data = this.data;

  switch (data && data.action) {
    case "exec":
      if (!data["sql"]) {
        throw new Error("exec: Missing query string");
      }

        // Simulate SQL operations using in-memory store
        const { sql, params } = data;
        let results = [];

        if (sql.startsWith("SELECT")) {
          // Simplified SELECT (assuming simple key-value lookup)
          const key = params[0]; // Assuming the first param is the key
          results = memoryStore[key] ? [{ values: [[memoryStore[key]]] }] : [{ values: [] }];
        } else if (sql.startsWith("INSERT")) {
          // Simplified INSERT (assuming key-value storage)
          const key = params[0]; // Assuming the first param is the key
          const value = params[1]; // Assuming the second param is the value
          memoryStore[key] = value;
        } else if (sql.startsWith("DELETE")) {
          // Simplified DELETE (assuming key-value deletion)
          const key = params[0]; // Assuming the first param is the key
          delete memoryStore[key];
        }

      return postMessage({
        id: data.id,
        results: results[0] ?? { values: [] }
      });
    case "begin_transaction":
      return postMessage({
        id: data.id,
        results: {}
      });
    case "end_transaction":
      return postMessage({
        id: data.id,
        results: {}
      });
    case "rollback_transaction":
      return postMessage({
        id: data.id,
        results: {}
      });
    default:
      throw new Error(`Unsupported action: ${data && data.action}`);
  }
}

function onError(err) {
  return postMessage({
    id: this.data.id,
    error: err
  });
}

if (typeof importScripts === "function") {
  db = null;
  const sqlModuleReady = createDatabase()
  self.onmessage = (event) => {
    return sqlModuleReady
      .then(onModuleReady.bind(event))
      .catch(onError.bind(event));
  }
}