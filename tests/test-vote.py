#!/usr/bin/env python3

import requests
import uuid
import random
import sys

def cast_vote(poll_id, user_id, option):
    """
    Cast a vote for the specified poll ID, user ID, and option.
    """
    try:
        response = requests.post(f"http://localhost:8080/poll_votes", json={
            "poll_id": poll_id,
            "user_id": user_id,
            "option": option,
        })
        if response.status_code == 200:
            return f"Vote successful: poll_id={poll_id}, user_id={user_id}, option={option}"
        else:
            return f"Vote failed: poll_id={poll_id}, user_id={user_id}, option={option}, error={response.text}"
    except Exception as e:
        return f"Vote failed: poll_id={poll_id}, user_id={user_id}, option={option}, error={e}"

def simulate_votes(poll_id, mode, n_votes, n_opts, user_id=None):
    """
    Simulate voting based on the specified mode.

    Args:
        poll_id (str): UUID of the poll.
        mode (str): Voting mode ("same_user" or "different_users").
        n_votes (int): Number of votes to cast.
        n_opts (int): Number of options in the poll.
        user_id (str): Specific user ID (used only in "same_user" mode).
    """
    results = []
    if mode == "same_user":
        # Use the provided user_id or generate one if missing
        user_id = user_id or str(uuid.uuid4())
        for _ in range(n_votes):
            option = random.randint(0, n_opts - 1)
            results.append(cast_vote(poll_id, user_id, option))
    elif mode == "different_users":
        # Generate a unique user_id for each vote
        for _ in range(n_votes):
            user_id = str(uuid.uuid4())
            option = random.randint(0, n_opts - 1)
            results.append(cast_vote(poll_id, user_id, option))
    return results

if __name__ == "__main__":
    if len(sys.argv) < 4:
        print("Usage: ./vote_attack.py <poll_id> <mode> <n_votes> [user_id]")
        print("Modes: same_user, different_users")
        sys.exit(1)

    poll_id = sys.argv[1]
    mode = sys.argv[2]
    n_votes = int(sys.argv[3])
    n_opts = 4  # Number of options in the poll (can be adjusted)

    # Optional user_id for "same_user" mode
    user_id = sys.argv[4] if len(sys.argv) > 4 else None

    if mode not in ["same_user", "different_users"]:
        print("Invalid mode. Use 'same_user' or 'different_users'.")
        sys.exit(1)

    print(f"Starting {n_votes} votes on poll {poll_id} using mode: {mode}")
    if user_id:
        print(f"Using user_id: {user_id}")

    results = simulate_votes(poll_id, mode, n_votes, n_opts, user_id)
    print("\n".join(results))
