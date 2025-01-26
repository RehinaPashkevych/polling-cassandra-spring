#!/usr/bin/env python3

import requests
import uuid
import random

def add_random_poll():
    poll = {
        "question": str(uuid.uuid4()),
        "answers": [str(uuid.uuid4()) for i in range(random.randint(3, 5))],
        "ttl": random.randint(30, 60),
    }
    poll_ret = requests.post("http://localhost:8080/polls", json=poll).json()
    print(f"Created poll with {poll_ret['id']} id")
    return poll_ret

def get_poll(poll_id):
    return requests.get(f"http://localhost:8080/polls/{poll_id}").json()

def cast_vote(poll_id, user_id, option):
    requests.post(f"http://localhost:8080/poll_votes", json={
        "poll_id": poll_id,
        "user_id": user_id,
        "option": option,
    })

def get_results(poll_id):
    response = requests.get(f"http://localhost:8080/poll_results/{poll_id}")
    return response.json(), response.headers.get("X-Final-Results")

if __name__ == "__main__":
    poll = add_random_poll()
    n_opts = len(poll["answers"])
    for i in range(1000):
        cast_vote(poll["id"], str(uuid.uuid4()), i % n_opts)
    body, final = get_results(poll["id"])
    print(body)
    print(final)
