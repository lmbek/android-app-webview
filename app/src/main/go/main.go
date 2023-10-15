package main

import (
	"fmt"
	"net/http"
)

func main() {
	http.HandleFunc("/", func(w http.ResponseWriter, r *http.Request) {
		fmt.Fprintf(w, "Hello from Go server!")
	})

	// Start the HTTP server on port 8080
	fmt.Println("Go server listening on :8080")
	http.ListenAndServe("localhost:8080", nil)
}
