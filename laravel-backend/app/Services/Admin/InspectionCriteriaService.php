<?php

namespace App\Services\Admin;

use App\Exceptions\HandlerException;
use App\Repositories\Admin\InspectionCriteriaRepository;

class InspectionCriteriaService
{
    protected $inspectionCriteriaRepository;

    public function __construct(InspectionCriteriaRepository $inspectionCriteriaRepository)
    {
        $this->inspectionCriteriaRepository = $inspectionCriteriaRepository;
    }

    public function create(array $data)
    {
        return $this->inspectionCriteriaRepository->create($data);
    }

    public function delete($id)
    {
        $item = $this->inspectionCriteriaRepository->findById($id);

        if (!$item) {
            throw new HandlerException('Inspection Criteria not found.', 404);
        }

        $result = $this->inspectionCriteriaRepository->delete($item);

        return $result;
    }

    public function update($id, array $data)
    {
        $item = $this->inspectionCriteriaRepository->findById($id);

        if (!$item) {
            throw new HandlerException('Inspection Criteria not found.', 404);
        }

        $updated = $this->inspectionCriteriaRepository->update($item, $data);

        return $updated;
    }

    public function getAll()
    {
        return $this->inspectionCriteriaRepository->all();
    }

    public function getById($id)
    {
        return $this->inspectionCriteriaRepository->findById($id);
    }
}