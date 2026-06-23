<?php

namespace App\Http\Controllers\Client;

use App\Http\Controllers\Controller;
use App\Http\Requests\Client\Comment\CreateCommentRequest;
use Illuminate\Http\Request;

class ClientCommentController extends Controller
{
    public function create(CreateCommentRequest $request)
    {
        $validateData = $request->validated();

        return response()->json(['data' => $request->content], 200);
    }

    public function testing(Request $request) {
        return response()->json($request->all(), 200);
    }
}
