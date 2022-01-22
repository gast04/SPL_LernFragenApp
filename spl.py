import json

with open("SPL/Fr_Deu.MMM", "rb") as f:
  raw_file = f.read()

# remove header
raw_questions = raw_file[81:]

questions_json = {}

off = 0
while True:
  question_json = {}

  if (off + 21 > len(raw_questions)):
    break

  correct_answer = raw_questions[off + 21]
  # index of correct answer, shifted value 1<<answer
  if(correct_answer == 1):
    correct_answer = 0
  if (correct_answer == 2):
    correct_answer = 1
  if (correct_answer == 4):
    correct_answer = 2
  if (correct_answer == 8):
    correct_answer = 3

  q_length = int.from_bytes(raw_questions[off+24:off+24+4],"big")

  header = raw_questions[off:off+24]
  #print(header)

  off += 24+4
  question = raw_questions[off:off + q_length]
  print("{} ({})".format(question.decode("iso-8859-1"), correct_answer))

  # answers
  off += q_length

  answers_json = {}
  for i in range(4):
    a_length = raw_questions[off+1] # actually two bytes
    answer = raw_questions[off+2:off+2+a_length]
    print("{}: {}".format(i, answer.decode("iso-8859-1")))
    off += 2 + a_length
    answers_json[i] = answer.decode("iso-8859-1")

  q_num_length = raw_questions[off] # one byte
  q_number = raw_questions[off+1: off+1+q_num_length]

  print("Question: {}\n".format(q_number.decode("iso-8859-1")))
  off += 1 + q_num_length

  question_json["question"] = question.decode("iso-8859-1")
  question_json["correct"] = correct_answer
  question_json["answers"] = answers_json
  questions_json[q_number.decode("iso-8859-1")] = question_json

print("finished parsing questions, dump to spl_question.json")
with open("spl_question.json", "w+") as spl:
  json.dump(questions_json, spl)
